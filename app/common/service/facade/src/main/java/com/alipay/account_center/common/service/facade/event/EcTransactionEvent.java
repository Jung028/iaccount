package com.alipay.account_center.common.service.facade.event;

import com.alipay.account_center.common.service.facade.enums.TxnEventType;

import java.math.BigDecimal;
import java.util.Date;

public class EcTransactionEvent {
    private String txnId;
    private String payerAccountNo;
    private String payeeAccountNo;
    private BigDecimal amount;
    private String txnStatus;
    private String currency;
    private String failReason;
    private String gmtTaskOccur;
    private String txnEventType;
    private boolean feeActive;
    private String txnCategory;

    public EcTransactionEvent(String txnId, String payerAccountNo, String payeeAccountNo, BigDecimal amount, String currency, String txnEventType, boolean feeActive) {
        this.txnId = txnId;
        this.payerAccountNo = payerAccountNo;
        this.payeeAccountNo = payeeAccountNo;
        this.amount = amount;
        this.currency = currency;
        this.txnEventType = txnEventType;
        this.feeActive = feeActive;
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

    public String getTxnStatus() {
        return txnStatus;
    }

    public void setTxnStatus(String txnStatus) {
        this.txnStatus = txnStatus;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public String getGmtTaskOccur() {
        return gmtTaskOccur;
    }

    public void setGmtTaskOccur(String gmtTaskOccur) {
        this.gmtTaskOccur = gmtTaskOccur;
    }

    public String getTxnEventType() {
        return txnEventType;
    }

    public void setTxnEventType(String txnEventType) {
        this.txnEventType = txnEventType;
    }

    public boolean isFeeActive() {
        return feeActive;
    }

    public void setFeeActive(boolean feeActive) {
        this.feeActive = feeActive;
    }

    public String getTxnCategory() {
        return txnCategory;
    }

    public void setTxnCategory(String txnCategory) {
        this.txnCategory = txnCategory;
    }
}
