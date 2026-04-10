package com.alipay.account_center.biz.service.impl.transaction.handler;

import com.alipay.account_center.common.service.facade.enums.TransactionType;
import com.alipay.account_center.common.service.facade.event.EcTransactionEvent;
import com.alipay.account_center.common.service.facade.pair.LockPair;
import com.alipay.account_center.core.model.domain.pair.AccountPair;

/**
 * @author adam
 * @date 7/4/2026 6:12 PM
 */
public interface TransactionalHandler {

    TransactionType getType();
    
    LockPair setFirstAndSecondLock(EcTransactionEvent event);

    AccountPair updateAccountRecord(EcTransactionEvent event);

}