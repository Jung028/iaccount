package com.alipay.account_center.core.model.enums;

public enum AccountActionEnum {
    TRANSFER("TRANSFER", "Transfer between accounts"),
    TOP_UP("TOP_UP", "top up"),
    QUERY_ACCOUNT_INFO("QUERY_ACCOUNT_INFO", "Query account info"),
    CREATE_ACCOUNT("CREATE_ACCOUNT", "Create account"),
    QUERY_TRANSACTION_HISTORY("QUERY_TRANSACTION_HISTORY", "Query transaction history"),
    QUERY_TRANSACTION_RECORD("QUERY_TRANSACTION_RECORD", "Query transaction record"),
    INSERT_TRANSACTION_RECORD("INSERT_TRANSACTION_RECORD", "Insert new transaction record"),
    UPDATE_TRANSACTION_RECORD("UPDATE_TRANSACTION_RECORD", "update transaction record"),
    PUBLISH_TRANSFER_EVENT("PUBLISH_TRANSFER_EVENT", "publish transfer event"),
    QUERY_TRANSACTION_BY_STATUS("QUERY_TRANSACTION_BY_STATUS", "Query transaction record by status list");

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
