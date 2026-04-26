package com.alipay.account_center.common.service.facade.enums;

/**
 * @author adam
 * @date 13/3/2026 12:08 AM
 */
public enum LedgerEntryTypeEnum {
    DEBIT("DEBIT", "deduct"),
    CREDIT("CREDIT", "add");

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

    LedgerEntryTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}