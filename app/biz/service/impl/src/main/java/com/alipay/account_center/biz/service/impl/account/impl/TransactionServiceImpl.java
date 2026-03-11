package com.alipay.account_center.biz.service.impl.account.impl;

import com.alipay.account_center.biz.service.impl.account.TransactionService;
import com.alipay.account_center.biz.service.impl.lock.DistributedLock;
import com.alipay.account_center.common.service.facade.enums.TransactionStatusEnum;
import com.alipay.account_center.common.service.facade.event.EcDlqEvent;
import com.alipay.account_center.common.service.facade.request.InsertLedgerRequest;
import com.alipay.account_center.common.service.facade.request.QueryTransactionRecordRequest;
import com.alipay.account_center.common.service.facade.request.UpdateTransactionRecordRequest;
import com.alipay.account_center.common.service.integration.wallet.WalletServiceClient;
import com.alipay.account_center.common.util.LogUtil;
import com.alipay.account_center.core.model.domain.AccountInfo;
import com.alipay.account_center.core.model.domain.TransactionRecord;
import com.alipay.account_center.common.service.facade.event.EcTransactionEvent;
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

import java.util.HashMap;
import java.util.Map;

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

        boolean locked = distributedLock.tryLock(txnId, 5000); // TTL 5s
        if (!locked) {
            throw new IllegalStateException("Unable to acquire lock for txnId: " + txnId);
        }

        BusinessBizResult<IdempotencyKeysItem> queryIdempotencyKeysResult = null;
        try {
            // check the idemptency record whether the status is in pending otherwise exit
            QueryIdempotencyKeysRequest queryIdempotencyKeysRequest = new QueryIdempotencyKeysRequest();
            queryIdempotencyKeysRequest.setTxnId(event.getTxnId());
            queryIdempotencyKeysResult = walletServiceClient.queryIdempotencyKeys(queryIdempotencyKeysRequest);
            if (queryIdempotencyKeysResult == null || !queryIdempotencyKeysResult.isSuccess()) {
                throw new RuntimeException();
            }
            if (!queryIdempotencyKeysResult.getResult().getStatus().equals(IdempotencyKeysStatusEnum.PENDING.getCode())
                    && !(queryIdempotencyKeysResult.getResult().getRetryCount() < MAX_RETRY_COUNT)) {
                throw new IllegalStateException("illegal state for idempotency keys, should be INIT status");
            }
            // else, update status to processing
            UpdateIdempotencyKeysRequest request = new UpdateIdempotencyKeysRequest();
            request.setTxnId(event.getTxnId());
            request.setStatus(IdempotencyKeysStatusEnum.PROCESSING);
            BusinessBizResult<UpdateIdempotencyKeysResult> updateIdempotencyKeysResult = walletServiceClient.updateIdempotencyKey(request);
            if  (updateIdempotencyKeysResult.getResult() == null) {
                throw new IllegalArgumentException("failed to update idempotency table");
            }

            //query transaction record checkits in OTP_OVER_LIMIT or PENDING otherwise reject
            QueryTransactionRecordRequest queryTransactionRecordRequest = new QueryTransactionRecordRequest();
            queryTransactionRecordRequest.setTxnId(event.getTxnId());
            TransactionRecord transactionRecord = accountTransactionRepository.queryTransactionRecord(queryTransactionRecordRequest);
            if (transactionRecord == null) {
                throw new RuntimeException("transaction record is null");
            }
            if (!transactionRecord.getTxnStatus().equals(TransactionStatusEnum.PENDING)
                    && !transactionRecord.getTxnStatus().equals(TransactionStatusEnum.OTP_OVER_LIMIT)) {
                throw new IllegalStateException("transaction status is " + transactionRecord.getTxnStatus());
            }

            String firstLock = event.getPayeeAccountNo().compareTo(event.getPayerAccountNo()) > 0 ? event.getPayeeAccountNo() : event.getPayerAccountNo();
            String secondLock = event.getPayeeAccountNo().compareTo(event.getPayerAccountNo()) > 0 ? event.getPayerAccountNo() : event.getPayeeAccountNo();

            boolean firstLockKey = distributedLock.tryLock(firstLock, 5000);
            boolean secondLockKey = distributedLock.tryLock(secondLock, 5000);

            if (!firstLockKey || !secondLockKey) {
                if (firstLockKey) distributedLock.unlock(firstLock);
                if (secondLockKey) distributedLock.unlock(secondLock);
                throw new IllegalStateException("failed to acquire lock for txnId: " + event.getTxnId() + ", lock : " + firstLock + ", secondLock: " + secondLock);
            }

            try {
                transactionTemplate.execute(status -> {
                    // lock the account record
                    AccountInfo payer = accountRepository.lockById(event.getPayerAccountNo());
                    AccountInfo payee = accountRepository.lockById(event.getPayeeAccountNo());

                    // debit and credit check balance
                    payer.debit(event.getAmount());
                    payee.credit(event.getAmount());

                    // then only update the account database
                    accountRepository.updateAccountRecord(payer);
                    accountRepository.updateAccountRecord(payee);

                    // then set transaction status success and unlock
                    UpdateTransactionRecordRequest updateTransactionRecord = new UpdateTransactionRecordRequest();
                    updateTransactionRecord.setStatus(TransactionStatusEnum.FINISH.getCode());
                    accountTransactionRepository.updateTransactionRecord(updateTransactionRecord);

                    InsertLedgerRequest insertLedgerRequest = new InsertLedgerRequest();
                    insertLedgerRequest.setTxnId(event.getTxnId());
                    insertLedgerRequest.setPayerAccountNo(event.getPayerAccountNo());
                    insertLedgerRequest.setPayeeAccountNo(event.getPayeeAccountNo());
                    insertLedgerRequest.setAmount(event.getAmount());
                    accountLedgerRepository.insertLedger(insertLedgerRequest);

                    return null;
                });

            } finally {
                // unlock
                distributedLock.unlock(firstLock);
                distributedLock.unlock(secondLock);
            }
        } catch (RuntimeException e) {
            UpdateTransactionRecordRequest updateTransactionRecord = new UpdateTransactionRecordRequest();
            updateTransactionRecord.setTxnId(event.getTxnId());
            updateTransactionRecord.setFailReason("Update Idempotency Keys failed");
            updateTransactionRecord.setStatus(TransactionStatusEnum.FAILED.getCode());
            accountTransactionRepository.updateTransactionRecord(updateTransactionRecord);

            // 2. Try to update idempotency keys if available
            BusinessBizResult<UpdateIdempotencyKeysResult> result;
            if (queryIdempotencyKeysResult != null && queryIdempotencyKeysResult.getResult() != null) {
                UpdateIdempotencyKeysRequest request = new UpdateIdempotencyKeysRequest();
                request.setTxnId(queryIdempotencyKeysResult.getResult().getTxnId());
                request.setStatus(IdempotencyKeysStatusEnum.FAILED);
                request.setRetryCount(queryIdempotencyKeysResult.getResult().getRetryCount() + 1);
                result = walletServiceClient.updateIdempotencyKey(request);
                if (result == null || !result.isSuccess() && result.getResult() != null) {
                    LogUtil.error(logger, "Failed to update idempotency keys for txnId: " + event.getTxnId());
                    return;
                }

                // only if the retry count exceeds max, then send to dead letter queue
                if (result.getResult().getRetryCount() > MAX_RETRY_COUNT) {
                    EcDlqEvent dlqEvent = new EcDlqEvent();
                    dlqEvent.setSceneCode("TRANSFER_FAILED");
                    dlqEvent.setTxnId(txnId);
                    dlqEvent.setPayerAccountNo(event.getPayerAccountNo());
                    dlqEvent.setPayeeAccountNo(event.getPayeeAccountNo());
                    dlqEvent.setAmount(event.getAmount());
                    dlqEvent.setFailReason(e.getMessage());
                    dlqEvent.setGmtTaskOccur(String.valueOf(System.currentTimeMillis()));
                    Map<String, String> extInfo = new HashMap<>();
                    extInfo.put("ErrorMessage : ", e.getMessage());
                    extInfo.put("IdempotencyResult: ", result.getResultCode());
                    dlqEvent.setExtInfo(extInfo.toString());

                    // Publish to dlq
                    kafkaTemplate.send("EC_DEAD_LETTER_QUEUE", dlqEvent);
                }
            } else {
                LogUtil.error(logger, "Idempotency keys not found for txnId: " + event.getTxnId());
            }
        } finally {
            // unlock the lock
            distributedLock.unlock(txnId);
        }
    }

    /**
     * set account repository
     * @param accountRepository
     */
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * set account transaction repository
     * @param accountTransactionRepository
     */
    public void setAccountTransactionRepository(AccountTransactionRepository accountTransactionRepository) {
        this.accountTransactionRepository = accountTransactionRepository;
    }

    /**
     * set account ledger repository
     * @param accountLedgerRepository
     */
    public void setAccountLedgerRepository(AccountLedgerRepository accountLedgerRepository) {
        this.accountLedgerRepository = accountLedgerRepository;
    }

    /**
     * set distributed lock
     * @param distributedLock
     */
    public void setDistributedLock(DistributedLock distributedLock) {
        this.distributedLock = distributedLock;
    }

    /**
     * set wallet service client
     * @param walletServiceClient
     */
    public void setWalletServiceClient(WalletServiceClient walletServiceClient) {
        this.walletServiceClient = walletServiceClient;
    }

    /**
     * set kafka template
     * @param kafkaTemplate
     */
    public void setKafkaTemplate(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * set transactional template
     * @param transactionTemplate
     */
    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }
}
