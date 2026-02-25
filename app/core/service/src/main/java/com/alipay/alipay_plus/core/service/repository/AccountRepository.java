package com.alipay.alipay_plus.core.service.repository;

import com.alipay.alipay_plus.common.service.facade.request.*;
import com.alipay.alipay_plus.core.model.domain.AccountInfo;
import com.alipay.alipay_plus.core.model.domain.TransactionHistory;
import com.alipay.alipay_plus.core.model.domain.TransactionRecord;

import java.util.List;

public interface AccountRepository {
    AccountInfo queryAccountInfo(String accountNo);

    AccountInfo createAccount(CreateAccountRequest request);


    AccountInfo lockById(String accountId);

    void updateAccountRecord(AccountInfo accountInfo);
}
