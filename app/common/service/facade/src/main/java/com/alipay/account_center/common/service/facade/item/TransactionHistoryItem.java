package com.alipay.account_center.common.service.facade.item;

import com.alipay.account_center.common.service.facade.enums.TransactionDirectionEnum;
import com.alipay.account_center.common.service.facade.enums.TransactionStatusEnum;

import java.math.BigDecimal;
import java.util.Date;

public class TransactionHistoryItem {
    private String txnId;
    private Date gmtCreate;
    private BigDecimal amount;
    private String currency;
    private TransactionDirectionEnum direction;
    private TransactionStatusEnum status;
    private String desc;

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

    public TransactionDirectionEnum getDirection() {
        return direction;
    }

    public void setDirection(TransactionDirectionEnum direction) {
        this.direction = direction;
    }

    public TransactionStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TransactionStatusEnum status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
