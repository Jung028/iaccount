package com.alipay.alipay_plus.common.service.integration.wallet;

import com.alipay.alipay_plus.common.service.facade.enums.AccountResultCode;
import com.alipay.alipay_plus.core.model.util.AssertUtil;
import com.alipay.business.common.service.facade.api.BusinessService;
import com.alipay.business.common.service.facade.baseresult.BusinessBizResult;
import com.alipay.business.common.service.facade.item.IdempotencyKeysItem;
import com.alipay.business.common.service.facade.request.QueryIdempotencyKeysRequest;
import com.alipay.business.common.service.facade.request.UpdateIdempotencyKeysRequest;
import com.alipay.business.common.service.facade.result.UpdateIdempotencyKeysResult;

public class WalletServiceClientImpl implements WalletServiceClient {

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
