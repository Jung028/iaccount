package com.alipay.account_center.common.service.facade.enums;

public enum TransactionType {
    TRANSFER("TRANSFER", "transfer"),
    REFUND("REFUND", "refund"),
    DEPOSIT("DEPOSIT", "deposit"),
    TOP_UP("TOP_UP", "top_up"),


    ;
    private String code;
    private String desc;

    TransactionType(String code, String desc) {
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
