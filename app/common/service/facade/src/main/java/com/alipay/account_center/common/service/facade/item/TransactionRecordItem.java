package com.alipay.account_center.common.service.facade.item;

import com.alipay.account_center.common.service.facade.enums.TransactionCategory;
import com.alipay.account_center.common.service.facade.enums.TransactionDirection;
import com.alipay.account_center.common.service.facade.enums.TransactionStatusEnum;
import com.alipay.account_center.common.service.facade.enums.TransactionType;

import java.math.BigDecimal;
import java.util.Date;

public class TransactionRecordItem {

    private String txnId;
    private Date gmtCreate;
    private Date gmtModified;
    private Date gmtComplete;
    private String payerAccountId;
    private String payeeAccountId;
    private BigDecimal amount;
    private String currency;
    private TransactionType txnType;
    private TransactionStatusEnum txnStatus;
    private String failureReason;
    private String extInfo;
    private TransactionDirection txnDirection;
    private TransactionCategory txnCategory;

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Date getGmtComplete() {
        return gmtComplete;
    }

    public void setGmtComplete(Date gmtComplete) {
        this.gmtComplete = gmtComplete;
    }

    public String getPayerAccountId() {
        return payerAccountId;
    }

    public void setPayerAccountId(String payerAccountId) {
        this.payerAccountId = payerAccountId;
    }

    public String getPayeeAccountId() {
        return payeeAccountId;
    }

    public void setPayeeAccountId(String payeeAccountId) {
        this.payeeAccountId = payeeAccountId;
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

    public TransactionType getTxnType() {
        return txnType;
    }

    public void setTxnType(TransactionType txnType) {
        this.txnType = txnType;
    }

    public TransactionStatusEnum getTxnStatus() {
        return txnStatus;
    }

    public void setTxnStatus(TransactionStatusEnum txnStatus) {
        this.txnStatus = txnStatus;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public String getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo;
    }

    public TransactionDirection getTxnDirection() {
        return txnDirection;
    }

    public void setTxnDirection(TransactionDirection txnDirection) {
        this.txnDirection = txnDirection;
    }

    public TransactionCategory getTxnCategory() {
        return txnCategory;
    }

    public void setTxnCategory(TransactionCategory txnCategory) {
        this.txnCategory = txnCategory;
    }
}
