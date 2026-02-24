package com.alipay.alipay_plus.biz.service.impl.account;

import com.alipay.alipay_plus.common.service.facade.event.EcTransactionEvent;

public interface TransactionService {
    void processTransfer(EcTransactionEvent event);
}
