package com.alipay.alipay_plus.biz.service.impl.account.impl;

import com.alipay.alipay_plus.biz.service.impl.account.TransactionService;
import com.alipay.alipay_plus.biz.service.impl.lock.DistributedLock;
import com.alipay.alipay_plus.common.service.facade.enums.TransactionStatusEnum;
import com.alipay.alipay_plus.common.service.facade.request.UpdateTransactionRecordRequest;
import com.alipay.alipay_plus.core.model.domain.AccountInfo;
import com.alipay.alipay_plus.core.model.domain.TransactionRecord;
import com.alipay.alipay_plus.common.service.facade.event.EcTransactionEvent;
import com.alipay.alipay_plus.core.service.repository.AccountRepository;
import org.apache.ibatis.annotations.Update;

public class TransactionServiceImpl implements TransactionService {

    private AccountRepository accountRepository;

    private DistributedLock distributedLock;

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
            TransactionRecord transactionRecord = accountRepository.updateTransactionRecord(request);

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
            accountRepository.updateTransactionRecord(updateTransactionRecord);
        } catch (RuntimeException e) {
            UpdateTransactionRecordRequest updateTransactionRecord = new UpdateTransactionRecordRequest();
            updateTransactionRecord.setTxnId(event.getTxnId());
            updateTransactionRecord.setStatus(TransactionStatusEnum.FAILED.getCode());
            accountRepository.updateTransactionRecord(updateTransactionRecord);
        } finally {
            // unlock the lock
            distributedLock.unlock(txnId);
        }
    }
}
