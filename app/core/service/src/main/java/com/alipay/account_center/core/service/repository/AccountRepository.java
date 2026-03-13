package com.alipay.account_center.core.service.repository;

import com.alipay.account_center.common.service.facade.request.CreateAccountRequest;
import com.alipay.account_center.core.model.domain.AccountInfo;

public interface AccountRepository {
    AccountInfo queryAccountInfo(String accountNo);

    AccountInfo createAccount(CreateAccountRequest request);

    AccountInfo lockById(String accountId);

    int updateAccountRecord(AccountInfo accountInfo);
}
