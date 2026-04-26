package com.alipay.account_center.biz.service.impl.transaction.handler;

import com.alipay.account_center.biz.service.impl.account.impl.AbstractAccountBizService;
import com.alipay.account_center.common.service.facade.enums.TransactionType;
import com.alipay.account_center.common.service.facade.event.EcTransactionEvent;
import com.alipay.account_center.common.service.facade.pair.LockPair;
import com.alipay.account_center.core.model.domain.AccountInfo;
import com.alipay.account_center.core.model.domain.pair.AccountPair;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author adam
 * @date 7/4/2026 6:11 PM
 */
@Component
public class TopUpHandler extends AbstractAccountBizService implements TransactionalHandler{

    @Override
    public TransactionType getType() {
        return TransactionType.TOP_UP;
    }

    @Override
    public LockPair setFirstAndSecondLock(EcTransactionEvent event) {
        String firstLock = event.getPayeeAccountNo().compareTo(event.getPayerAccountNo()) > 0
                ? event.getPayeeAccountNo() : event.getPayerAccountNo();
        
        boolean firstLocked = distributedLock.tryLock(firstLock, 5000);
        if (!firstLocked) {
            throw new IllegalStateException(                    
                    "Failed to acquire account locks for txnId: " + event.getTxnId());
        }
        return new LockPair(firstLock, "");
    }

    @Override
    public AccountPair updateAccountRecord(EcTransactionEvent event) {
        // we only update the payee account, since payer is the STRIPE_CLEARING_ACCOUNT
        AccountInfo payer = accountRepository.lockById(event.getPayerAccountNo());
        AccountInfo payee = accountRepository.lockById(event.getPayeeAccountNo());
        payee.credit(event.getAmount());
        payee.setGmtModified(new Date());
        
        accountRepository.updateAccountRecord(payee);
        return new AccountPair(payer, payee);
    }
}