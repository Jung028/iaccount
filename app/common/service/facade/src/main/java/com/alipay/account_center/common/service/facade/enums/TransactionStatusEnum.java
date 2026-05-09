package com.alipay.account_center.common.service.facade.enums;

public enum TransactionStatusEnum {
    OTP_OVER_LIMIT("OTP_OVER_LIMIT", "require OTP verification for transfer amount over limit"),
    PENDING("PENDING", "transaction record is pending"),
    FINISH("FINISH", "finished processing this transaction "),
    FAILED("FAILED", "Unexpected exception occurred, transaction has failed ");

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
        this.code = code;
        this.desc = desc;
    }

    public static TransactionStatusEnum fromCode(String code) {
        if (code == null) {
            return PENDING;
        }
        for (TransactionStatusEnum status : values()) {
            if (status.code.equalsIgnoreCase(code)) {
                return status;
            }
        }
        return PENDING;
    }
}
