package com.alipay.account_center.common.service.integration.user;

import com.alipay.usercenter.common.service.facade.baseresult.UserBizResult;
import com.alipay.usercenter.common.service.facade.item.AutoReloadConfigItem;
import com.alipay.usercenter.common.service.facade.request.QueryAutoReloadConfigRequest;

/**
 * @author adam
 * @date 26/3/2026 6:55 PM
 */
public interface TopUpServiceClient {
    UserBizResult<AutoReloadConfigItem> queryAutoReloadConfig(QueryAutoReloadConfigRequest queryAutoReloadConfigRequest);
}