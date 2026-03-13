package com.alipay.account_center.common.service.facade.event;

import java.math.BigDecimal;

public class EcTransactionEvent {
    private String txnId;
    private String payerAccountNo;
    private String payeeAccountNo;
    private BigDecimal amount;
    private String currency;

    public EcTransactionEvent(String txnId, String payerAccountNo, String payeeAccountNo, BigDecimal amount, String currency) {
        this.txnId = txnId;
        this.payerAccountNo = payerAccountNo;
        this.payeeAccountNo = payeeAccountNo;
        this.amount = amount;
        this.currency = currency;
    }

    public EcTransactionEvent() {
        // default constructor needed by Jackson
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getPayerAccountNo() {
        return payerAccountNo;
    }

    public void setPayerAccountNo(String payerAccountNo) {
        this.payerAccountNo = payerAccountNo;
    }

    public String getPayeeAccountNo() {
        return payeeAccountNo;
    }

    public void setPayeeAccountNo(String payeeAccountNo) {
        this.payeeAccountNo = payeeAccountNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
}

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
