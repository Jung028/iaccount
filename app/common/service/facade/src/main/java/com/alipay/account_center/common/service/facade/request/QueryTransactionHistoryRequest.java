package com.alipay.account_center.common.service.facade.request;

public class QueryTransactionHistoryRequest extends QueryPageBaseRequest {
    private String accountId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
