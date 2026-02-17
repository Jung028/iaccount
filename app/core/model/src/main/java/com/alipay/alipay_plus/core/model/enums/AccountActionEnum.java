package com.alipay.alipay_plus.core.model.enums;

public enum AccountActionEnum {
    TRANSFER("TRANSFER", "Transfer between accounts"),
    DEPOSIT("DEPOSIT", "Deposit to account"),
    QUERY_ACCOUNT_INFO("QUERY_ACCOUNT_INFO", "Query account info"),
    CREATE_ACCOUNT("CREATE_ACCOUNT", "Create account"),
    QUERY_TRANSACTION_HISTORY("QUERY_TRANSACTION_HISTORY", "Query transaction history"),
    QUERY_TRANSACTION_RECORD("QUERY_TRANSACTION_RECORD", "Query transaction record");

    private String code;
    private String desc;

    AccountActionEnum(String code, String desc) {
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
