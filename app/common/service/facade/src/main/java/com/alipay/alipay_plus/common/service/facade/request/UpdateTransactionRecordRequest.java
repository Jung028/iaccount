package com.alipay.alipay_plus.common.service.facade.request;

import com.alipay.alipay_plus.common.service.facade.baseresult.AccountBaseRequest;

public class UpdateTransactionRecordRequest extends AccountBaseRequest {
    private String txnId;
    private String status;

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
