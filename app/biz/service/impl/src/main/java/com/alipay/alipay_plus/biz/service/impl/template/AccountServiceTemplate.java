package com.alipay.alipay_plus.biz.service.impl.template;

import com.alipay.alipay_plus.biz.service.impl.helper.AccountResultHelper;
import com.alipay.alipay_plus.common.service.facade.baseresult.AccountBaseRequest;
import com.alipay.alipay_plus.common.service.facade.baseresult.AccountBaseResult;
import com.alipay.alipay_plus.common.util.LogUtil;
import com.alipay.alipay_plus.common.service.facade.constant.LoggerConstant;
import com.alipay.alipay_plus.common.service.facade.enums.AccountResultCode;
import com.alipay.alipay_plus.common.util.TenantUtil;
import com.alipay.alipay_plus.common.util.enums.IpayTenantEnum;
import com.alipay.alipay_plus.core.model.context.IdigitalriskContextHolder;

import com.alipay.alipay_plus.core.model.enums.AccountActionEnum;
import com.alipay.alipay_plus.core.model.exception.IdigitalriskException;
import com.alipay.alipay_plus.common.util.EventContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class AccountServiceTemplate {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerConstant.RISK_BIZ_SERVICE_LOG);

    /**
     * slipExtraDAO
     */
    //protected SlipExtraDAO slipExtraDAO;

    /**
     * execute
     *
     * @param request digital risk request
     * @param action digital risk action
     * @param callback digital risk biz callback
     * @return digital risk result
     */
    public <T extends AccountBaseRequest, R extends AccountBaseResult> R execute(
            final T request,
            final AccountActionEnum action,
            final AccountBizCallback<T, R> callback) {

        R result = callback.createDefaultResponse();

        LogUtil.info(LOGGER, "service request[", request, "]");

        try {
            callback.checkParams(request);

            initContext(action, request);

            callback.process(request, result);

            AccountResultHelper.fillSuccessResultCode(result);

        } catch (IdigitalriskException e) {

            LogUtil.warn(LOGGER, e, "service process exception[", request, "]", ", code = "
                    , e.getResultCode(), ", msg= ", e.getMessage());

            AccountResultHelper.fillExceptionResultCode(result, e.getResultCode());

        } catch (Throwable e) {
            LogUtil.error(LOGGER, e, "service process unexpected exception[", request, "]");

            AccountResultHelper.fillExceptionResultCode(result, AccountResultCode.SYSTEM_EXCEPTION);

        } finally {
            printDigestLog(result);

            LogUtil.info(LOGGER, "service result[" , result , "] [request =", request, "]" );

            IdigitalriskContextHolder.clear();
        }


        return result;
    }

    private <R extends AccountBaseResult> void printDigestLog(R result) {
    }

    private <T extends AccountBaseRequest, R extends AccountBaseResult> void initContext(AccountActionEnum action, T request) {
        EventContext context = TenantUtil.getCurrentEventContext();
        context.setTntInstId(IpayTenantEnum.IPAY_SG.getTntInstId());
        TenantUtil.setCurrentEventContext(context);
       // IdigitalriskContextHolder.set(action, slipExtraDAO.updateAndGetSystemDate(),request.getOperatorId(), request.getOperatorName());

    }
}
