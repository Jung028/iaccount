package com.alipay.account_center.common.service.facade.enums;

/**
 * @author adam
 * @date 16/3/2026 12:54 AM
 */
public enum AccountTypeEnum {
    SAVINGS("SAVINGS", "savings account"),
    MERCHANT_ACCOUNT("MERCHANT_ACCOUNT", "merchant account"),
    IPAY_ACCOUNT("IPAY_ACCOUNT", "company account");


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

    AccountTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}