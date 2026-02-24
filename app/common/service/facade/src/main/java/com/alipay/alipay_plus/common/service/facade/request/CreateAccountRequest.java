package com.alipay.alipay_plus.common.service.facade.request;

import com.alipay.alipay_plus.common.service.facade.baseresult.AccountBaseRequest;

import java.math.BigDecimal;

public class CreateAccountRequest extends AccountBaseRequest {
    private String accountNumber;
    private String accountName;
    private String accountType;
    private String accRelationId;
    private String currency;
    private BigDecimal balance;
    private String desc;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccRelationId() {
        return accRelationId;
    }

    public void setAccRelationId(String accRelationId) {
        this.accRelationId = accRelationId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
