package com.alipay.account_center.common.service.facade.request;

public class QueryTransactionHistoryRequest extends QueryPageBaseRequest {
    private String accountId;
    private String txnId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }
}
