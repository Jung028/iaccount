package com.alipay.alipay_plus.core.model.util;

import com.alipay.alipay_plus.common.service.facade.enums.AccountResultCode;
import com.alipay.alipay_plus.core.model.exception.BaseSlipException;
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
    public static interface AssertTemplate {
        public void doAssert();
    }
    private static void check(AssertTemplate assertTemplate, AccountResultCode accountResultCode, String resultMsg) {
        try {
            assertTemplate.doAssert();
        } catch (IllegalArgumentException e) {
            if (StringUtils.isBlank(resultMsg)) {
                throw new BaseSlipException(accountResultCode);
            } else {
                throw new BaseSlipException(accountResultCode, resultMsg);
            }
        }
    }
}
