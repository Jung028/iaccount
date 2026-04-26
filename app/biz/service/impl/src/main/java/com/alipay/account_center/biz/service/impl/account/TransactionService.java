package com.alipay.account_center.biz.service.impl.account;

import com.alipay.account_center.common.service.facade.event.EcTransactionEvent;


public interface TransactionService {

    void processTransfer(EcTransactionEvent event);

}
