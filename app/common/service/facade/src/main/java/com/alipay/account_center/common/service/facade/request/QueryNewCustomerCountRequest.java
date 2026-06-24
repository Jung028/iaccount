package com.alipay.account_center.common.service.facade.request;

import com.alipay.account_center.common.service.facade.baseresult.AccountBaseRequest;

public class QueryNewCustomerCountRequest extends AccountBaseRequest {
    private String merchantId;

    public String getMerchantId() { return merchantId; }
    public void setMerchantId(String merchantId) { this.merchantId = merchantId; }
}
