package com.alipay.alipay_plus.biz.service.impl.request;

import com.alipay.alipay_plus.common.service.facade.baseresult.AccountBaseRequest;

import java.math.BigDecimal;

/**
 * @author adam
 * @date 26/2/2026 8:01 PM
 */
public class InsertLedgerRequest extends AccountBaseRequest {
    private String txnId;
    private String payerAccountNo;
    private String payeeAccountNo;
    private BigDecimal amount;

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
}