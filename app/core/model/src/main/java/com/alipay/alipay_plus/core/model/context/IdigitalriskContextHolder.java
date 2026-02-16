package com.alipay.alipay_plus.core.model.context;

import com.alipay.alipay_plus.core.model.enums.AccountActionEnum;

import java.util.Date;

public final class IdigitalriskContextHolder {

    private final static ThreadLocal<IdigitalriskContext> contextLocal = new ThreadLocal<>();

    public static void set(IdigitalriskContext context){
        contextLocal.set(context);
    }

    public static void set(AccountActionEnum action, Date time, String operatorId, String operatorName) {
        set(new IdigitalriskContext(action, time, operatorId, operatorName));
    }

    public static void clear() {
        contextLocal.remove();
    }
}
