package com.alipay.account_center.common.service.facade.request;

import com.alipay.account_center.common.service.facade.baseresult.AccountBaseRequest;
import com.alipay.account_center.common.service.facade.enums.TransactionStatusEnum;
import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import java.math.BigDecimal;

public class InsertTransactionRecordRequest extends AccountBaseRequest {
    private String txnId;
    private String payeeAccountNo;
    private String payerAccountNo;
    private BigDecimal amount;
    private CurrencyUnit currency;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public CurrencyUnit getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyUnit currency) {
        this.currency = currency;
    }

    public TransactionStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TransactionStatusEnum status) {
        this.status = status;
    }
}
