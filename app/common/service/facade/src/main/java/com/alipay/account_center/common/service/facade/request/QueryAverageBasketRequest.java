package com.alipay.account_center.common.service.facade.request;

import com.alipay.account_center.common.service.facade.baseresult.AccountBaseRequest;

/**
 * @author adam
 * @date 24/6/2026 10:13 PM
 */
public class QueryAverageBasketRequest extends AccountBaseRequest {
    private String merchantId;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }
}
