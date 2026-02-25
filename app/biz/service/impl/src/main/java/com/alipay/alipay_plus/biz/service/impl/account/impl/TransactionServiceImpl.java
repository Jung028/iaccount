package com.alipay.alipay_plus.biz.service.impl.account.impl;

import com.alipay.alipay_plus.biz.service.impl.account.TransactionService;
import com.alipay.alipay_plus.biz.service.impl.lock.DistributedLock;
import com.alipay.alipay_plus.common.service.facade.enums.TransactionStatusEnum;
import com.alipay.alipay_plus.common.service.facade.request.UpdateTransactionRecordRequest;
import com.alipay.alipay_plus.common.service.integration.wallet.WalletServiceClient;
import com.alipay.alipay_plus.core.model.domain.AccountInfo;
import com.alipay.alipay_plus.core.model.domain.TransactionRecord;
import com.alipay.alipay_plus.common.service.facade.event.EcTransactionEvent;
import com.alipay.alipay_plus.core.service.repository.AccountRepository;
import com.alipay.alipay_plus.core.service.repository.AccountTransactionRepository;
import com.alipay.business.common.service.facade.baseresult.BusinessBizResult;
import com.alipay.business.common.service.facade.request.UpdateIdempotencyKeysRequest;

public class TransactionServiceImpl implements TransactionService {

    private AccountRepository accountRepository;

    private AccountTransactionRepository accountTransactionRepository;

    private DistributedLock distributedLock;

    private WalletServiceClient walletServiceClient;

    @Override
    public void processTransfer(EcTransactionEvent event) {
        String txnId = event.getTxnId();

        boolean locked = distributedLock.tryLock(txnId, 5000); // TTL 5s
        if (!locked) {
            throw new IllegalStateException("Unable to acquire lock for txnId: " + txnId);
        }

        try {
            UpdateTransactionRecordRequest request = new UpdateTransactionRecordRequest();
            request.setTxnId(event.getTxnId());
            // we receive the PENDING
            request.setStatus(TransactionStatusEnum.PENDING.getCode());
            // we try update where its processing
            TransactionRecord transactionRecord = accountTransactionRepository.updateTransactionRecord(request);

            // if the result = 0,
            if (transactionRecord == null || !transactionRecord.getFailureReason().isEmpty()) {
                throw new RuntimeException();
            }

            distributedLock.tryLock(transactionRecord.getTxnId(),1);
            // lock the account record
            AccountInfo payer = accountRepository.lockById(event.getPayerAccountNo());
            AccountInfo payee = accountRepository.lockById(event.getPayerAccountNo());

            // then we update the account domain first to check balance
            payer.debit(event.getAmount());
            payee.credit(event.getAmount());

            // then only update the account database
            accountRepository.updateAccountRecord(payer);
            accountRepository.updateAccountRecord(payee);

            // then set transaction status success and unlock
            UpdateTransactionRecordRequest updateTransactionRecord = new UpdateTransactionRecordRequest();
            updateTransactionRecord.setStatus(TransactionStatusEnum.FINISH.getCode());
            accountTransactionRepository.updateTransactionRecord(updateTransactionRecord);


            //    AccountCenter-->>ledger: if both result success, add in ledger(txnId, transferTo, transferFrom, amount)
            //    AccountCenter-->>Idempotency: update transaction status to FINISH, unlock
            //    AccountCenter-->>AccountCenter: return null, end of transaction template


        } catch (RuntimeException e) {
            UpdateIdempotencyKeysRequest request = new UpdateIdempotencyKeysRequest();
            BusinessBizResult<String> result = walletServiceClient.updateIdempotencyKey(request);
            if (result.isSuccess()) {
                UpdateTransactionRecordRequest updateTransactionRecord = new UpdateTransactionRecordRequest();
                updateTransactionRecord.setTxnId(event.getTxnId());
                updateTransactionRecord.setFailReason("Update Idempotency Keys failed");
                updateTransactionRecord.setStatus(TransactionStatusEnum.FAILED.getCode());
                accountTransactionRepository.updateTransactionRecord(updateTransactionRecord);
            }
            //TODO : implement send to DLQ after 3 retries and still failed. since this is a transaction
            //			  else retry account exceeded
            //				  MsgBroker-->>DLQ: send to dead letter queue after 3 retry and still failed
            //				  DLQ-->>RiskReport: report risk for business team to handle manually
            //		    end
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
     * set distributed lock
     * @param distributedLock
     */
    public void setDistributedLock(DistributedLock distributedLock) {
        this.distributedLock = distributedLock;
    }
}
