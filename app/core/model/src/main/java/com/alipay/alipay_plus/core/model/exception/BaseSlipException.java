package com.alipay.alipay_plus.core.model.exception;

import com.alipay.alipay_plus.common.service.facade.enums.AccountResultCode;

public class BaseSlipException extends RuntimeException {

    public BaseSlipException(AccountResultCode accountResultCode) {
        super(accountResultCode.getCode());

    }

    public BaseSlipException(AccountResultCode accountResultCode, String resultMsg) {
        super(accountResultCode.getCode() + ":" + resultMsg);
    }

}
