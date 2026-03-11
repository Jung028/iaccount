package com.alipay.account_center.common.service.integration.wallet;

import com.alipay.account_center.common.service.facade.api.AccountService;
import com.alipay.account_center.common.service.facade.enums.AccountResultCode;
import com.alipay.account_center.core.model.util.AssertUtil;
import com.alipay.business.common.service.facade.api.BusinessService;
import com.alipay.business.common.service.facade.baseresult.BusinessBizResult;
import com.alipay.business.common.service.facade.item.IdempotencyKeysItem;
import com.alipay.business.common.service.facade.request.QueryIdempotencyKeysRequest;
import com.alipay.business.common.service.facade.request.UpdateIdempotencyKeysRequest;
import com.alipay.business.common.service.facade.result.UpdateIdempotencyKeysResult;
import com.alipay.sofa.runtime.api.annotation.SofaReference;
import com.alipay.sofa.runtime.api.annotation.SofaReferenceBinding;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletServiceClientImpl implements WalletServiceClient {

    @SofaReference(interfaceType = BusinessService.class,
            binding = @SofaReferenceBinding(bindingType = "rest", directUrl = "http://127.0.0.1:8343"),
            jvmFirst = true)
    private BusinessService businessService;


    @Override
    public BusinessBizResult<UpdateIdempotencyKeysResult> updateIdempotencyKey(UpdateIdempotencyKeysRequest request) {
        AssertUtil.notNull(request, AccountResultCode.PARAM_ILLEGAL, "update idempotency keys request is null");

        //set cross invoke
        BusinessBizResult<UpdateIdempotencyKeysResult> result = businessService.updateIdempotencyKeys(request);
        AssertUtil.notNull(result, AccountResultCode.PARAM_ILLEGAL, ", result is null");
        AssertUtil.notNull(result.getResult(), AccountResultCode.PARAM_ILLEGAL, ", result is null");
        AssertUtil.isTrue(result.isSuccess(), AccountResultCode.PARAM_ILLEGAL, ", result is not success");
        return result;
    }

    @Override
    public BusinessBizResult<IdempotencyKeysItem> queryIdempotencyKeys(QueryIdempotencyKeysRequest request) {

        BusinessBizResult<IdempotencyKeysItem> result = businessService.queryIdempotencyKeys(request);
        AssertUtil.notNull(result, AccountResultCode.PARAM_ILLEGAL, ", result is null");
        AssertUtil.notNull(result.getResult(), AccountResultCode.PARAM_ILLEGAL, ", result is null");
        AssertUtil.isTrue(result.isSuccess(), AccountResultCode.PARAM_ILLEGAL, ", result is not success");
        return result;
    }
}
