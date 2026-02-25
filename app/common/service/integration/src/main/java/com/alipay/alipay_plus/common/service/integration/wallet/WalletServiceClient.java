package com.alipay.alipay_plus.common.service.integration.wallet;

import com.alipay.business.common.service.facade.baseresult.BusinessBizResult;
import com.alipay.business.common.service.facade.request.UpdateIdempotencyKeysRequest;

public interface WalletServiceClient {
    BusinessBizResult<String> updateIdempotencyKey(UpdateIdempotencyKeysRequest request);
}
