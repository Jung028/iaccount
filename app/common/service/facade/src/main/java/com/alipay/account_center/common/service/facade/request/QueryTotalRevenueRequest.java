package com.alipay.account_center.common.service.facade.request;

import com.alipay.account_center.common.service.facade.baseresult.AccountPageBaseRequest;

import java.time.LocalDateTime;

/**
 * @author adam
 * @date 23/6/2026 11:56 PM
 */
public class QueryTotalRevenueRequest extends AccountPageBaseRequest {
    private String merchantId;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

}
