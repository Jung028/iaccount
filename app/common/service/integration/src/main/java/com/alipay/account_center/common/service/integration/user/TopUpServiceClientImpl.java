package com.alipay.account_center.common.service.integration.user;

import com.alipay.account_center.common.service.facade.api.AccountService;
import com.alipay.account_center.common.service.facade.enums.AccountResultCode;
import com.alipay.account_center.core.model.util.AssertUtil;
import com.alipay.business.common.service.facade.baseresult.BusinessBizResult;
import com.alipay.business.common.service.facade.result.UpdateIdempotencyKeysResult;
import com.alipay.sofa.runtime.api.annotation.SofaReference;
import com.alipay.sofa.runtime.api.annotation.SofaReferenceBinding;
import com.alipay.usercenter.common.service.facade.api.TopUpService;
import com.alipay.usercenter.common.service.facade.baseresult.UserBizResult;
import com.alipay.usercenter.common.service.facade.item.AutoReloadConfigItem;
import com.alipay.usercenter.common.service.facade.request.QueryAutoReloadConfigRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author adam
 * @date 26/3/2026 6:56 PM
 */
@Service
public class TopUpServiceClientImpl implements TopUpServiceClient {

    /**
     * Top Up Service
     */
    @SofaReference(interfaceType = TopUpService.class,
            binding = @SofaReferenceBinding(bindingType = "rest", directUrl = "http://127.0.0.1:8342"),
            jvmFirst = true)
    protected TopUpService topUpService;

    @Override
    public UserBizResult<AutoReloadConfigItem> queryAutoReloadConfig(QueryAutoReloadConfigRequest request) {
        AssertUtil.notNull(request, AccountResultCode.PARAM_ILLEGAL, "query auto reload config is null");
        AssertUtil.notBlank(request.getUserId(), AccountResultCode.PARAM_ILLEGAL, "userId cannot be blank");

        //set cross invoke
        UserBizResult<AutoReloadConfigItem> result = topUpService.queryAutoReloadConfig(request);
        AssertUtil.notNull(result, AccountResultCode.PARAM_ILLEGAL, ", result is null");
        AssertUtil.isTrue(result.isSuccess(), AccountResultCode.PARAM_ILLEGAL, ", result is not success");
        return result;
    }
}