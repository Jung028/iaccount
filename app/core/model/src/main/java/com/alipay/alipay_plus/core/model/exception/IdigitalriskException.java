package com.alipay.alipay_plus.core.model.exception;

import com.alipay.alipay_plus.common.service.facade.enums.AccountResultCode;

public class IdigitalriskException extends RuntimeException {

  private static final long seralVersionUID = 9187623791824214L;

  private AccountResultCode resultCode;

  public IdigitalriskException(AccountResultCode resultCode, String message) {
    super(message);
    this.resultCode = resultCode;
  }

  public IdigitalriskException(AccountResultCode resultCode) {
    this(resultCode, resultCode.getDescription());
  }

  public AccountResultCode getResultCode() {
    return resultCode;
  }

  public void setResultCode(AccountResultCode resultCode) {
    this.resultCode = resultCode;
  }
}
