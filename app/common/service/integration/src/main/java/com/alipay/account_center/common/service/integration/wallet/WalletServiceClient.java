package com.alipay.account_center.common.service.integration.wallet;

import com.alipay.business.common.service.facade.baseresult.BusinessBizResult;
import com.alipay.business.common.service.facade.item.IdempotencyKeysItem;
import com.alipay.business.common.service.facade.request.QueryIdempotencyKeysRequest;
import com.alipay.business.common.service.facade.request.UpdateIdempotencyKeysRequest;
import com.alipay.business.common.service.facade.result.UpdateIdempotencyKeysResult;

public interface WalletServiceClient {
    BusinessBizResult<UpdateIdempotencyKeysResult> updateIdempotencyKey(UpdateIdempotencyKeysRequest request);
    BusinessBizResult<IdempotencyKeysItem> queryIdempotencyKeys(QueryIdempotencyKeysRequest request);
}
