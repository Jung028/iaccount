package com.alipay.account_center.core.model.util;

import com.alipay.account_center.common.service.facade.enums.AccountResultCode;
import com.alipay.account_center.core.model.exception.BaseSlipException;
import com.alipay.account_center.core.model.exception.IdigitalriskException;
import io.micrometer.common.util.StringUtils;
import org.springframework.util.Assert;

public class AssertUtil {

    public static void notNull(final Object object, final AccountResultCode accountResultCode, final String resultMsg) {
        check(new AssertTemplate() {
            @Override
            public void doAssert() {
                Assert.notNull(object, "resultMsg");
            }
        }, accountResultCode, resultMsg);
    }

    public static void isTrue(final boolean expression, AccountResultCode accountStatusIllegal, String accountStatusIsNotValid) {
        check(() -> Assert.isTrue(expression,"is true"), accountStatusIllegal, accountStatusIsNotValid);
    }

    public static void isTrue(final boolean expression, final String resultCode,
                              final String resultMsg) {
        check(() -> Assert.isTrue(expression,"is true"), AccountResultCode.valueOf(resultCode), resultMsg);
    }

    public static void notBlank(final String str, final AccountResultCode accountResultCode, final String resultMsg) {
        check(() -> Assert.isTrue(StringUtils.isNotBlank(str),"is true"),
                accountResultCode, resultMsg);
    }

    public static void isTrue(boolean success, String s) {
        check(() -> Assert.isTrue(success,"is true"), AccountResultCode.valueOf(s), s);
    }

    public static interface AssertTemplate {
        public void doAssert();

    }

    private static void check(AssertTemplate assertTemplate, AccountResultCode AccountResultCode, String resultMsg) {
        try {
            assertTemplate.doAssert();
        } catch (IllegalArgumentException e) {
            if (StringUtils.isBlank(resultMsg)) {
                throw new IdigitalriskException(AccountResultCode);
            } else {
                throw new BaseSlipException(AccountResultCode, resultMsg);
            }
        }
    }
}
