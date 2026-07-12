package com.alipay.account_center.biz.service.impl.account.impl;

import com.alipay.account_center.biz.service.impl.account.TransactionService;
import com.alipay.account_center.biz.service.impl.transaction.factory.TransactionHandlerFactory;
import com.alipay.account_center.biz.service.impl.transaction.handler.TransactionalHandler;
import com.alipay.account_center.common.service.facade.enums.AccountResultCode;
import com.alipay.account_center.common.service.facade.enums.TransactionDirection;
import com.alipay.account_center.common.service.facade.enums.TransactionStatusEnum;
import com.alipay.account_center.common.service.facade.event.EcDlqEvent;
import com.alipay.account_center.common.service.facade.item.LedgerEntryItem;
import com.alipay.account_center.common.service.facade.pair.LockPair;
import com.alipay.account_center.common.service.facade.request.QueryTransactionRecordRequest;
import com.alipay.account_center.common.service.facade.request.UpdateTransactionRecordRequest;
import com.alipay.account_center.common.util.LogUtil;
import com.alipay.account_center.core.model.domain.TransactionRecord;
import com.alipay.account_center.common.service.facade.event.EcTransactionEvent;
import com.alipay.account_center.core.model.domain.pair.AccountPair;
import com.alipay.account_center.core.model.util.AssertUtil;
import com.alipay.business.common.service.facade.baseresult.BusinessBizResult;
import com.alipay.business.common.service.facade.enums.IdempotencyKeysStatusEnum;
import com.alipay.business.common.service.facade.item.IdempotencyKeysItem;
import com.alipay.business.common.service.facade.request.QueryIdempotencyKeysRequest;
import com.alipay.business.common.service.facade.request.UpdateIdempotencyKeysRequest;
import com.alipay.business.common.service.facade.result.UpdateIdempotencyKeysResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.alipay.account_center.biz.service.impl.constant.GlobalBizConstant.MAX_RETRY_COUNT;

/**
 * @author adam
 * @date 26/2/2026 7:46 PM
 */
@Service
public class TransactionServiceImpl extends AbstractAccountBizService implements TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    private TransactionHandlerFactory transactionHandlerFactory;

    @Override
    public void processTransfer(EcTransactionEvent event) {
        String txnId = event.getTxnId();
        System.out.println("START TRANSACTION");
        // ── lock the transaction id ────────────────────────────────────────────
        boolean txnLocked = distributedLock.tryLock(txnId, 5000);
        if (!txnLocked) {
            throw new IllegalStateException("Unable to acquire lock for txnId: " + txnId);
        }
        // get handler
        System.out.println(event.getTxnEventType());
        // TODO: here, it AssertUtil not null becuse the handler is null.
        TransactionalHandler handler = transactionHandlerFactory.getHandler(event.getTxnEventType());
        System.out.println("HANDLER: " + handler.getClass().getName());

        // lock only payee since top up payer account is from bank, not account
        LockPair lockPair = handler.setFirstAndSecondLock(event);
        String firstLock = lockPair.getFirstLock();
        String secondLock = lockPair.getSecondLock();



        // build the result event — will be published in the finally block
        EcTransactionEvent resultEvent = new EcTransactionEvent();
        resultEvent.setTxnId(event.getTxnId());
        resultEvent.setPayerAccountNo(event.getPayerAccountNo());
        resultEvent.setPayeeAccountNo(event.getPayeeAccountNo());
        resultEvent.setAmount(event.getAmount());
        resultEvent.setCurrency(event.getCurrency());
        resultEvent.setGmtTaskOccur(String.valueOf(System.currentTimeMillis()));
        resultEvent.setTxnCategory(event.getTxnCategory());
        System.out.println("TXN_CATEGORY: " + event.getTxnCategory());

        BusinessBizResult<IdempotencyKeysItem> queryIdempotencyKeysResult = null;

        try {
            // Must be PENDING and within retry limit to proceed.
            QueryIdempotencyKeysRequest queryIdempotencyKeysRequest =
                    new QueryIdempotencyKeysRequest();
            System.out.println(event.getTxnId());
            queryIdempotencyKeysRequest.setReferenceId(event.getTxnId());
            queryIdempotencyKeysResult =
                    walletServiceClient.queryIdempotencyKeys(queryIdempotencyKeysRequest);
            System.out.println(queryIdempotencyKeysResult.getResult().getReferenceId());

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
            markProcessing.setReferenceId(event.getTxnId());
            markProcessing.setStatus(IdempotencyKeysStatusEnum.PROCESSING);
            BusinessBizResult<UpdateIdempotencyKeysResult> processingResult =
                    walletServiceClient.updateIdempotencyKey(markProcessing);

            AssertUtil.isTrue(processingResult != null && processingResult.isSuccess(),
                    AccountResultCode.SYSTEM_EXCEPTION, "update idempotency key failed :" + txnId);

            QueryTransactionRecordRequest queryTxnRequest = new QueryTransactionRecordRequest();
            queryTxnRequest.setTxnId(event.getTxnId());
            queryTxnRequest.setAccountId(event.getPayeeAccountNo());
            TransactionRecord transactionRecord =
                    accountTransactionRepository.queryTransactionRecord(queryTxnRequest);

            AssertUtil.notNull(transactionRecord, AccountResultCode.PARAM_ILLEGAL, txnId);
            AssertUtil.isTrue(
                    transactionRecord.getTxnStatus().equals(TransactionStatusEnum.PENDING),
            AccountResultCode.ILLEGAL_STATUS, "Should be in pending status" + txnId);

            // ── debit payer, credit payee, write ledger entries ────────────────
            transactionTemplate.execute(status -> {
                // update account record
                AccountPair accountPair = handler.updateAccountRecord(event);

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
                payerLedger.setEntryType(TransactionDirection.DEBIT.getCode());
                payerLedger.setAmount(event.getAmount());
                payerLedger.setBalanceAfter(accountPair.getPayer().getBalance());
                payerLedger.setCurrency(event.getCurrency());
                accountLedgerRepository.insertLedger(payerLedger);

                // payee ledger — money arriving = CREDIT
                LedgerEntryItem payeeLedger = new LedgerEntryItem();
                payeeLedger.setTxnId(event.getTxnId());
                payeeLedger.setAccountId(event.getPayeeAccountNo());
                payeeLedger.setEntryType(TransactionDirection.CREDIT.getCode());
                payeeLedger.setAmount(event.getAmount());
                payeeLedger.setBalanceAfter(accountPair.getPayee().getBalance());
                payeeLedger.setCurrency(event.getCurrency());
                accountLedgerRepository.insertLedger(payeeLedger);


                // mark idempotency key as SUCCESS
                UpdateIdempotencyKeysRequest markSuccess = new UpdateIdempotencyKeysRequest();
                markSuccess.setReferenceId(event.getTxnId());
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
                markFailed.setReferenceId(queryIdempotencyKeysResult.getResult().getReferenceId());
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
                            failedResult.getResultCode());
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
            System.out.println(resultEvent.getTxnStatus());
            // publish result back to frontend via Kafka
            kafkaTemplate.send("EC_TRANSACTION_RESULT", event.getPayerAccountNo(), resultEvent);
        }
    }



}
