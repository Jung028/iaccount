package com.alipay.account_center.common.service.facade.request;

import com.alipay.account_center.common.service.facade.enums.TransactionCategory;
import com.alipay.account_center.common.service.facade.enums.TransactionStatusEnum;
import com.alipay.account_center.common.service.facade.enums.TransactionType;

public class QueryTransactionHistoryRequest extends QueryPageBaseRequest {
    private String accountId;
    private String payerAccountId;
    private String gmtCreate;
    private TransactionCategory category;
    private String amountMin;
    private String amountMax;
    private TransactionStatusEnum txnStatus;
    private TransactionType txnType;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPayerAccountId() {
        return payerAccountId;
    }

    public void setPayerAccountId(String payerAccountId) {
        this.payerAccountId = payerAccountId;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAmountMin() {
        return amountMin;
    }

    public void setAmountMin(String amountMin) {
        this.amountMin = amountMin;
    }

    public String getAmountMax() {
        return amountMax;
    }

    public void setAmountMax(String amountMax) {
        this.amountMax = amountMax;
    }

    public TransactionStatusEnum getTxnStatus() {
        return txnStatus;
    }

    public void setTxnStatus(TransactionStatusEnum txnStatus) {
        this.txnStatus = txnStatus;
    }

    public TransactionType getTxnType() {
        return txnType;
    }

    public void setTxnType(TransactionType txnType) {
        this.txnType = txnType;
    }
}
