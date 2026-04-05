package com.alipay.account_center.biz.service.impl.message;

import com.alipay.account_center.biz.service.impl.account.TopUpService;
import com.alipay.business.common.service.facade.event.EcTopUpEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author adam
 * @date 4/4/2026 6:03 PM
 */
@Component
public class EcTopUpListener {

    @Autowired
    private TopUpService topUpService;

    @KafkaListener(
            topics = "EC_TOPUP_RECEIVED",
            groupId = "account-center")
    public void topUp(EcTopUpEvent event) {
        // update balance, then publish EC_TOP_UP_RESULT, to allow frontend to know that top up is completed
        topUpService.processTopUpBalance(event);
    }
}