package com.alipay.alipay_plus.common.service.facade.enums;

public enum TransactionStatusEnum {
    OTP_OVER_LIMIT("OTP_OVER_LIMIT", "require OTP verification for transfer amount over limit"),
    PENDING("PENDING", "transaction record is pending"),

    ;

    private String code;
    private String desc;

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

    TransactionStatusEnum(String code, String desc) {

    }
}
