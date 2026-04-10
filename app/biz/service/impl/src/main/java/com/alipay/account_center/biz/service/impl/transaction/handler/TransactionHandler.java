package com.alipay.account_center.biz.service.impl.transaction.handler;

import com.alipay.account_center.biz.service.impl.account.impl.AbstractAccountBizService;
import com.alipay.account_center.common.service.facade.enums.TransactionType;
import com.alipay.account_center.common.service.facade.event.EcTransactionEvent;
import com.alipay.account_center.common.service.facade.pair.LockPair;
import com.alipay.account_center.core.model.domain.AccountInfo;
import com.alipay.account_center.core.model.domain.pair.AccountPair;
import com.alipay.business.common.service.facade.event.EcAutoReloadEvent;
import com.alipay.usercenter.common.service.facade.baseresult.UserBizResult;
import com.alipay.usercenter.common.service.facade.item.AutoReloadConfigItem;
import com.alipay.usercenter.common.service.facade.request.QueryAutoReloadConfigRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author adam
 * @date 7/4/2026 6:11 PM
 */
@Component
public class TransactionHandler extends AbstractAccountBizService implements TransactionalHandler {

    @Override
    public TransactionType getType() {
        return TransactionType.TRANSFER;
    }

    @Override
    public LockPair setFirstAndSecondLock(EcTransactionEvent event) {

        // ── lock payer and payee accounts in consistent order (prevents deadlock) ─
        String firstLock = event.getPayeeAccountNo().compareTo(event.getPayerAccountNo()) > 0
                ? event.getPayeeAccountNo() : event.getPayerAccountNo();
        String secondLock = event.getPayeeAccountNo().compareTo(event.getPayerAccountNo()) > 0
                ? event.getPayerAccountNo() : event.getPayeeAccountNo();
        
        boolean firstLocked  = distributedLock.tryLock(firstLock,  5000);
        boolean secondLocked = distributedLock.tryLock(secondLock, 5000);

        if (!firstLocked || !secondLocked) {
            if (firstLocked)  distributedLock.unlock(firstLock);
            if (secondLocked) distributedLock.unlock(secondLock);
            throw new IllegalStateException(
                    "Failed to acquire account locks for txnId: " + event.getTxnId());
        }
        return new LockPair(firstLock, secondLock);
    }

    @Override
    public AccountPair updateAccountRecord(EcTransactionEvent event) {

        // lock both accounts
        AccountInfo payer = accountRepository.lockById(event.getPayerAccountNo());
        AccountInfo payee = accountRepository.lockById(event.getPayeeAccountNo());

        // debit, credit payer and payee account
        payer.debit(event.getAmount());
        payee.credit(event.getAmount());
        payer.setGmtModified(new Date());
        payee.setGmtModified(new Date());

        accountRepository.updateAccountRecord(payer);
        accountRepository.updateAccountRecord(payee);
        
        // if balance is less than threshold amount, call chargeCard
        checkAndTriggerAutoReload(payer.getAccountRelationId(), payer.getBalance());

        return new AccountPair(payer, payee);
    }

    /**
     * check balance against threshold
     * @param accountRelationId
     * @param balanceAfter
     */
    private void checkAndTriggerAutoReload(String accountRelationId, BigDecimal balanceAfter) {
        QueryAutoReloadConfigRequest queryAutoReloadConfigRequest = new QueryAutoReloadConfigRequest();
        queryAutoReloadConfigRequest.setUserId(accountRelationId);
        UserBizResult<AutoReloadConfigItem> autoReloadConfig =
                topUpServiceClient.queryAutoReloadConfig(queryAutoReloadConfigRequest);
        if (autoReloadConfig != null && autoReloadConfig.getResult().getIsActive().equals(true)) {
            System.out.println(autoReloadConfig.getResult().getUserId());
            // if balanceAfter < thresholdAmount
            if (balanceAfter.compareTo(autoReloadConfig.getResult().getThresholdAmount()) < 0) {
                // publish Event
                EcAutoReloadEvent autoReloadEvent = new EcAutoReloadEvent();
                autoReloadEvent.setAmount(autoReloadConfig.getResult().getReloadAmount());
                autoReloadEvent.setCurrency(autoReloadConfig.getResult().getCurrency());
                autoReloadEvent.setUserId(accountRelationId);
                kafkaTemplate.send("EC_AUTO_RELOAD", autoReloadEvent);
            }
        }
    }
}