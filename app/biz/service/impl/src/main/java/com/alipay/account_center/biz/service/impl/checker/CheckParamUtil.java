package com.alipay.account_center.biz.service.impl.checker;

import com.alipay.account_center.common.service.facade.baseresult.AccountBaseRequest;
import com.alipay.account_center.common.service.facade.enums.AccountResultCode;
import com.alipay.account_center.common.service.facade.request.*;
import com.alipay.account_center.core.model.enums.AccountActionEnum;
import com.alipay.account_center.core.model.util.AssertUtil;

public class CheckParamUtil {

    public static void checkCreateAccountRequest(CreateAccountRequest request) {
        AssertUtil.notBlank(request.getCurrency(), AccountResultCode.PARAM_ILLEGAL, "currency cannot be blank");
        AssertUtil.isTrue(request.getCurrency().length() == 3, AccountResultCode.PARAM_ILLEGAL, "currency should be 3 char");
    }

    public static void checkQueryAccountInfoRequest(QueryAccountInfoRequest request) {
    }

    public static void checkQueryTransactionRecordRequest(QueryTransactionRecordRequest request) {
    }

    public static void checkQueryTransactionHistoryRequest(QueryTransactionHistoryRequest request) {

    }

    public static void checkInsertTransactionRecordRequest() {

    }

    public static void checkUpdateTransactionRecordRequest(AccountBaseRequest request) {

    }

    public static void checkPublishTransferRequest(PublishTransferRequest request) {

    }

    public static void checkQueryTransactionByStatus(QueryTransactionRecordRequest request) {

    }
}
