package com.alipay.account_center.biz.service.impl.account;

import com.alipay.business.common.service.facade.event.EcTopUpEvent;

/**
 * @author adam
 * @date 4/4/2026 6:01 PM
 */
public interface TopUpService {
    void  processTopUpBalance(EcTopUpEvent event);
}