package com.alipay.alipay_plus.common.service.facade.request;

import com.alipay.alipay_plus.common.service.facade.baseresult.AccountBaseRequest;

public class QueryAccountInfoRequest extends AccountBaseRequest {
    private String accountId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
