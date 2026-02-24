package com.alipay.alipay_plus.common.service.integration.message;

import com.alipay.alipay_plus.biz.service.impl.account.TransactionService;
import com.alipay.alipay_plus.common.service.facade.event.EcTransactionEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EcTransactionListener {

    private final TransactionService transactionService;

    @KafkaListener(
            topics = "EC_TRANSACTION",
            groupId = "account-center")
    public void transfer(EcTransactionEvent event) {
        transactionService.processTransfer(event);

    }

    public EcTransactionListener(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
}
