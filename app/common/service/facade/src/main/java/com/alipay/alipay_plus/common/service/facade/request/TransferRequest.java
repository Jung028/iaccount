package com.alipay.alipay_plus.common.service.facade.request;

import com.alipay.alipay_plus.common.service.facade.baseresult.AccountBaseRequest;

public class TransferRequest extends AccountBaseRequest {
    private String fromAccountNo;
    private String toAccountNo;
    private String amount;
    private String currency;

    public String getFromAccountNo() {
        return fromAccountNo;
    }

    public void setFromAccountNo(String fromAccountNo) {
        this.fromAccountNo = fromAccountNo;
    }

    public String getToAccountNo() {
        return toAccountNo;
    }

    public void setToAccountNo(String toAccountNo) {
        this.toAccountNo = toAccountNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
