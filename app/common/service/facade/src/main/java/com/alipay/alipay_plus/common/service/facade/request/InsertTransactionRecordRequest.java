package com.alipay.alipay_plus.common.service.facade.request;

import com.alipay.alipay_plus.common.service.facade.baseresult.AccountBaseRequest;
import com.alipay.alipay_plus.common.service.facade.enums.TransactionStatusEnum;

import javax.money.MonetaryAmount;

public class InsertTransactionRecordRequest extends AccountBaseRequest {
    private String txnId;
    private String payeeAccountNo;
    private String payerAccountNo;
    private MonetaryAmount amount;
    private TransactionStatusEnum status;

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getPayeeAccountNo() {
        return payeeAccountNo;
    }

    public void setPayeeAccountNo(String payeeAccountNo) {
        this.payeeAccountNo = payeeAccountNo;
    }

    public String getPayerAccountNo() {
        return payerAccountNo;
    }

    public void setPayerAccountNo(String payerAccountNo) {
        this.payerAccountNo = payerAccountNo;
    }

    public MonetaryAmount getAmount() {
        return amount;
    }

    public void setAmount(MonetaryAmount amount) {
        this.amount = amount;
    }

    public TransactionStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TransactionStatusEnum status) {
        this.status = status;
    }
}
