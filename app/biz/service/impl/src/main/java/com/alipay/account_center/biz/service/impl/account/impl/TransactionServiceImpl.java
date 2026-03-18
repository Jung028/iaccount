package com.alipay.account_center.biz.service.impl.account.impl;

import com.alipay.account_center.biz.service.impl.account.TransactionService;
import com.alipay.account_center.biz.service.impl.lock.DistributedLock;
import com.alipay.account_center.common.service.facade.enums.AccountResultCode;
import com.alipay.account_center.common.service.facade.enums.LedgerEntryTypeEnum;
import com.alipay.account_center.common.service.facade.enums.TransactionStatusEnum;
import com.alipay.account_center.common.service.facade.event.EcDlqEvent;
import com.alipay.account_center.common.service.facade.item.LedgerEntryItem;
import com.alipay.account_center.common.service.facade.request.QueryTransactionRecordRequest;
import com.alipay.account_center.common.service.facade.request.UpdateTransactionRecordRequest;
import com.alipay.account_center.common.service.integration.wallet.WalletServiceClient;
import com.alipay.account_center.common.util.LogUtil;
import com.alipay.account_center.core.model.domain.AccountInfo;
import com.alipay.account_center.core.model.domain.TransactionRecord;
import com.alipay.account_center.common.service.facade.event.EcTransactionEvent;
import com.alipay.account_center.core.model.util.AssertUtil;
import com.alipay.account_center.core.service.repository.AccountLedgerRepository;
import com.alipay.account_center.core.service.repository.AccountRepository;
import com.alipay.account_center.core.service.repository.AccountTransactionRepository;
import com.alipay.business.common.service.facade.baseresult.BusinessBizResult;
import com.alipay.business.common.service.facade.enums.IdempotencyKeysStatusEnum;
import com.alipay.business.common.service.facade.item.IdempotencyKeysItem;
import com.alipay.business.common.service.facade.request.QueryIdempotencyKeysRequest;
import com.alipay.business.common.service.facade.request.UpdateIdempotencyKeysRequest;
import com.alipay.business.common.service.facade.result.UpdateIdempotencyKeysResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author adam
 * @date 26/2/2026 7:46 PM
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountTransactionRepository accountTransactionRepository;

    @Autowired
    private AccountLedgerRepository accountLedgerRepository;

    @Autowired
    private DistributedLock distributedLock;

    @Autowired
    private WalletServiceClient walletServiceClient;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private TransactionTemplate transactionTemplate;

    private static final int MAX_RETRY_COUNT = 3;

    @Override
    public void processTransfer(EcTransactionEvent event) {
        String txnId = event.getTxnId();
        System.out.println("START TRANSACTION");
        // ── lock the transaction id ────────────────────────────────────────────
        boolean txnLocked = distributedLock.tryLock(txnId, 5000);
        if (!txnLocked) {
            throw new IllegalStateException("Unable to acquire lock for txnId: " + txnId);
        }

        // ── lock payer and payee accounts in consistent order (prevents deadlock) ─
        String firstLock  = event.getPayeeAccountNo().compareTo(event.getPayerAccountNo()) > 0
                ? event.getPayeeAccountNo() : event.getPayerAccountNo();
        String secondLock = event.getPayeeAccountNo().compareTo(event.getPayerAccountNo()) > 0
                ? event.getPayerAccountNo() : event.getPayeeAccountNo();

        boolean firstLocked  = distributedLock.tryLock(firstLock,  5000);
        boolean secondLocked = distributedLock.tryLock(secondLock, 5000);

        if (!firstLocked || !secondLocked) {
            if (firstLocked)  distributedLock.unlock(firstLock);
            if (secondLocked) distributedLock.unlock(secondLock);
            throw new IllegalStateException(
                    "Failed to acquire account locks for txnId: " + txnId);
        }

        // build the result event — will be published in the finally block
        EcTransactionEvent resultEvent = new EcTransactionEvent();
        resultEvent.setTxnId(event.getTxnId());
        resultEvent.setPayerAccountNo(event.getPayerAccountNo());
        resultEvent.setPayeeAccountNo(event.getPayeeAccountNo());
        resultEvent.setAmount(event.getAmount());
        resultEvent.setCurrency(event.getCurrency());
        resultEvent.setGmtTaskOccur(String.valueOf(System.currentTimeMillis()));

        BusinessBizResult<IdempotencyKeysItem> queryIdempotencyKeysResult = null;

        try {
            // ── idempotency guard ──────────────────────────────────────────────
            // Must be PENDING and within retry limit to proceed.
            QueryIdempotencyKeysRequest queryIdempotencyKeysRequest =
                    new QueryIdempotencyKeysRequest();
            System.out.println(event.getTxnId());
            queryIdempotencyKeysRequest.setTxnId(event.getTxnId());
            queryIdempotencyKeysResult =
                    walletServiceClient.queryIdempotencyKeys(queryIdempotencyKeysRequest);
            System.out.println(queryIdempotencyKeysResult.getResult().getTxnId());

            boolean isPending = queryIdempotencyKeysResult.getResult().getStatus()
                    .equals(IdempotencyKeysStatusEnum.PENDING.getCode());
            boolean withinRetryLimit = queryIdempotencyKeysResult.getResult().getRetryCount()
                    < MAX_RETRY_COUNT;

            System.out.println(isPending);
            System.out.println(withinRetryLimit);
            AssertUtil.isTrue(isPending && withinRetryLimit, AccountResultCode.ILLEGAL_STATUS,
                    "either not pending or its not within retry limit");

            // mark as PROCESSING to prevent concurrent execution
            UpdateIdempotencyKeysRequest markProcessing = new UpdateIdempotencyKeysRequest();
            markProcessing.setTxnId(event.getTxnId());
            markProcessing.setStatus(IdempotencyKeysStatusEnum.PROCESSING);
            BusinessBizResult<UpdateIdempotencyKeysResult> processingResult =
                    walletServiceClient.updateIdempotencyKey(markProcessing);

            AssertUtil.isTrue(processingResult != null && processingResult.isSuccess(),
                    AccountResultCode.SYSTEM_EXCEPTION, "update idempotency key failed :" + txnId);

            // ── verify transaction record is still PENDING ─────────────────────
            QueryTransactionRecordRequest queryTxnRequest = new QueryTransactionRecordRequest();
            queryTxnRequest.setTxnId(event.getTxnId());
            queryTxnRequest.setAccountId(event.getPayerAccountNo());
            TransactionRecord transactionRecord =
                    accountTransactionRepository.queryTransactionRecord(queryTxnRequest);

            AssertUtil.notNull(transactionRecord, AccountResultCode.PARAM_ILLEGAL, txnId);
            AssertUtil.isTrue(
                    transactionRecord.getTxnStatus().equals(TransactionStatusEnum.PENDING),
            AccountResultCode.ILLEGAL_STATUS, "Should be in pending status" + txnId);

            // ── debit payer, credit payee, write ledger entries ────────────────
            transactionTemplate.execute(status -> {

                // row-level locks on both accounts
                AccountInfo payer = accountRepository.lockById(event.getPayerAccountNo());
                AccountInfo payee = accountRepository.lockById(event.getPayeeAccountNo());

                payer.debit(event.getAmount());
                payee.credit(event.getAmount());
                payer.setGmtModified(new Date());
                payee.setGmtModified(new Date());

                accountRepository.updateAccountRecord(payer);
                accountRepository.updateAccountRecord(payee);

                // mark transaction as finished
                UpdateTransactionRecordRequest finishTxn =
                        new UpdateTransactionRecordRequest();
                finishTxn.setTxnId(event.getTxnId());
                finishTxn.setStatus(TransactionStatusEnum.FINISH.getCode());
                accountTransactionRepository.updateTransactionRecord(finishTxn);

                // payer ledger — money leaving = DEBIT
                LedgerEntryItem payerLedger = new LedgerEntryItem();
                payerLedger.setTxnId(event.getTxnId());
                payerLedger.setAccountId(event.getPayerAccountNo());
                payerLedger.setEntryType(LedgerEntryTypeEnum.DEBIT.getCode());
                payerLedger.setAmount(event.getAmount());
                payerLedger.setBalanceAfter(payer.getBalance());
                payerLedger.setCurrency(event.getCurrency());
                accountLedgerRepository.insertLedger(payerLedger);

                // payee ledger — money arriving = CREDIT
                LedgerEntryItem payeeLedger = new LedgerEntryItem();
                payeeLedger.setTxnId(event.getTxnId());
                payeeLedger.setAccountId(event.getPayeeAccountNo());
                payeeLedger.setEntryType(LedgerEntryTypeEnum.CREDIT.getCode());
                payeeLedger.setAmount(event.getAmount());
                payeeLedger.setBalanceAfter(payee.getBalance());
                payeeLedger.setCurrency(event.getCurrency());
                accountLedgerRepository.insertLedger(payeeLedger);

                // mark idempotency key as SUCCESS
                UpdateIdempotencyKeysRequest markSuccess = new UpdateIdempotencyKeysRequest();
                markSuccess.setTxnId(event.getTxnId());
                markSuccess.setStatus(IdempotencyKeysStatusEnum.SUCCESS);
                BusinessBizResult<UpdateIdempotencyKeysResult> successResult =
                        walletServiceClient.updateIdempotencyKey(markSuccess);

                if (successResult == null || !successResult.isSuccess()) {
                    LogUtil.error(logger,
                            "Failed to mark idempotency key SUCCESS for txnId: " + txnId);
                    throw new RuntimeException(
                            "Failed to update idempotency key to SUCCESS for txnId: " + txnId);
                }

                return null;
            });

            resultEvent.setTxnStatus(TransactionStatusEnum.FINISH.getCode());
            System.out.println("FINISH TRANSACTION");

        } catch (RuntimeException e) {
            System.out.println("FAILED TRANSACTION");

            LogUtil.error(logger, "Transfer failed for txnId: " + txnId
                    + ", reason: " + e.getMessage());

            // mark transaction as FAILED
            UpdateTransactionRecordRequest failTxn = new UpdateTransactionRecordRequest();
            failTxn.setTxnId(event.getTxnId());
            failTxn.setStatus(TransactionStatusEnum.FAILED.getCode());
            failTxn.setFailReason(e.getMessage());
            accountTransactionRepository.updateTransactionRecord(failTxn);

            // increment retry count on idempotency key
            if (queryIdempotencyKeysResult != null
                    && queryIdempotencyKeysResult.getResult() != null) {

                int newRetryCount = queryIdempotencyKeysResult.getResult().getRetryCount() + 1;

                UpdateIdempotencyKeysRequest markFailed = new UpdateIdempotencyKeysRequest();
                markFailed.setTxnId(queryIdempotencyKeysResult.getResult().getTxnId());
                markFailed.setStatus(IdempotencyKeysStatusEnum.FAILED);
                markFailed.setRetryCount(newRetryCount);
                BusinessBizResult<UpdateIdempotencyKeysResult> failedResult =
                        walletServiceClient.updateIdempotencyKey(markFailed);

                // if retry limit exceeded, send to dead letter queue
                if (failedResult != null
                        && failedResult.getResult() != null
                        && failedResult.getResult().getRetryCount() > MAX_RETRY_COUNT) {

                    EcDlqEvent dlqEvent = new EcDlqEvent();
                    dlqEvent.setSceneCode("TRANSFER_FAILED");
                    dlqEvent.setTxnId(txnId);
                    dlqEvent.setPayerAccountNo(event.getPayerAccountNo());
                    dlqEvent.setPayeeAccountNo(event.getPayeeAccountNo());
                    dlqEvent.setAmount(event.getAmount());
                    dlqEvent.setFailReason(e.getMessage());
                    dlqEvent.setGmtTaskOccur(String.valueOf(System.currentTimeMillis()));

                    Map<String, String> extInfo = new HashMap<>();
                    extInfo.put("errorMessage", e.getMessage());
                    extInfo.put("idempotencyResult",
                            failedResult != null ? failedResult.getResultCode() : "unknown");
                    dlqEvent.setExtInfo(extInfo.toString());

                    kafkaTemplate.send("EC_DEAD_LETTER_QUEUE", dlqEvent);
                }
            } else {
                LogUtil.error(logger,
                        "Idempotency key not found during failure handling for txnId: " + txnId);
            }

            resultEvent.setTxnStatus(TransactionStatusEnum.FAILED.getCode());
            resultEvent.setFailReason(e.getMessage());

        } finally {
            // always unlock in reverse order of acquisition
            distributedLock.unlock(txnId);
            distributedLock.unlock(firstLock);
            distributedLock.unlock(secondLock);
            System.out.println("FINISH, PUBLISH TRANSACTION");

            // publish result back to frontend via Kafka
            kafkaTemplate.send("EC_TRANSACTION_RESULT", resultEvent);
        }
    }

}
