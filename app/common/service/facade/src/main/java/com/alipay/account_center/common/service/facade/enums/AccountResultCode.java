package com.alipay.account_center.common.service.facade.enums;


public enum AccountResultCode {

    /**
     * execute success
     */
    EXECUTE_SUCCESS(GlobalResultCodes.EXECUTE_SUCCESS, "Success"),

    /**
     * system exception
     */
    SYSTEM_EXCEPTION(GlobalResultCodes.SYSTEM_EXCEPTION, "System Exception"),

    /**
     * param illegal
     */
    PARAM_ILLEGAL(GlobalResultCodes.PARAM_ILLEGAL, "Parameter Illegal"),

    /**
     * Repeated submit
     */
    REPEATED_SUBMIT(GlobalResultCodes.REPEATED_SUBMIT, "Repeated Submit"),

    /**
     * Account not found
     */
    ACCOUNT_NOT_FOUND(GlobalResultCodes.PARAM_ILLEGAL, "Account not found"),

    /**
     * Account status illegal
     */
    ACCOUNT_STATUS_ILLEGAL(GlobalResultCodes.PARAM_ILLEGAL, "Account not found"),

    /**
     * Invalid request
     */
    INVALID_REQUEST(GlobalResultCodes.PARAM_ILLEGAL, "Not allowed to access account"),

    /**
     * illegal status
     */
    ILLEGAL_STATUS(GlobalResultCodes.PARAM_ILLEGAL, "Illegal status");


    private final String code;

    private final String description;

    public String getCode() {
        return code;
    }
    public String getDescription() {
        return description;
    }


    AccountResultCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

}

