package com.alipay.account_center.common.service.facade.request;

import com.alipay.account_center.common.service.facade.baseresult.AccountBaseRequest;

import java.util.List;

public class QueryTransactionRecordRequest extends AccountBaseRequest {
    private String accountId;
    private String txnId;
    private List<String> txnStatusList;

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

    public List<String> getTxnStatusList() {
        return txnStatusList;
    }

    public void setTxnStatusList(List<String> txnStatusList) {
        this.txnStatusList = txnStatusList;
    }
}
