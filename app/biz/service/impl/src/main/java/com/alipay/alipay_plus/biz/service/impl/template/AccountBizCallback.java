package com.alipay.alipay_plus.biz.service.impl.template;

import com.alipay.alipay_plus.common.service.facade.baseresult.AccountBaseRequest;
import com.alipay.alipay_plus.common.service.facade.baseresult.AccountBaseResult;
import com.alipay.alipay_plus.common.service.facade.item.AccountInfoItem;

public abstract class AccountBizCallback<T extends AccountBaseRequest, R extends AccountBaseResult>{

    /**
     * define the default response object
     */
    protected abstract R createDefaultResponse();

    /**
     * check params
     */
    protected abstract void checkParams(T request);

    /**
     * execute
     *
     * @return
     */
    protected abstract void process(T request, R response);



}
