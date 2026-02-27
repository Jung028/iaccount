package com.alipay.alipay_plus.common.service.facade.event;

import java.math.BigDecimal;

/**
 * @author adam
 * @date 28/2/2026 12:03 AM
 */
public class EcDlqEvent {
    private String txnId;
    private String failReason;
    private String payerAccountNo;
    private String payeeAccountNo;
    private BigDecimal amount;
    private String gmtTaskOccur;
    private String sceneCode;
    private String extInfo;

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
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

    public String getGmtTaskOccur() {
        return gmtTaskOccur;
    }

    public void setGmtTaskOccur(String gmtTaskOccur) {
        this.gmtTaskOccur = gmtTaskOccur;
    }

    public String getSceneCode() {
        return sceneCode;
    }

    public void setSceneCode(String sceneCode) {
        this.sceneCode = sceneCode;
    }

    public String getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo;
    }
}