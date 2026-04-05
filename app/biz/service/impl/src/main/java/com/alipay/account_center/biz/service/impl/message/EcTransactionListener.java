package com.alipay.account_center.biz.service.impl.message;

import com.alipay.account_center.biz.service.impl.account.TransactionService;
import com.alipay.account_center.common.service.facade.event.EcTransactionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EcTransactionListener {

    @Autowired
    private TransactionService transactionService;

    @KafkaListener(
            topics = "EC_TRANSACTION",
            groupId = "business-center")
    public void transfer(EcTransactionEvent event) {
        transactionService.processTransfer(event);

    }

    public EcTransactionListener(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
}
