package com.alipay.account_center.common.service.facade.baseresult;

public class AccountBizResult<T> extends AccountBaseResult {
    private T result;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
