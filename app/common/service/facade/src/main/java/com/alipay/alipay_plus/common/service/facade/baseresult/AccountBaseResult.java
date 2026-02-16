package com.alipay.alipay_plus.common.service.facade.baseresult;

public class AccountBaseResult {
    private String resultMessage;
    private String resultCode;

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }
}

