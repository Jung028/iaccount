package com.alipay.account_center.common.service.facade.enums;

/**
 * @author adam
 * @date 7/4/2026 6:25 PM
 */
public enum TxnEventType {
    TRANSFER("TRANSFER", "transfer"),
    TOP_UP("TOP_UP", "top up");

    private String code;
    private String desc;

    TxnEventType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}