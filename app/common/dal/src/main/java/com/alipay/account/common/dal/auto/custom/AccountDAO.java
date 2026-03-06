package com.alipay.account.common.dal.auto.custom;

import com.alipay.account.common.dal.auto.dataobject.AccountDO;

public interface AccountDAO {
    AccountDO queryAccountInfo(String accountId);

    AccountDO createAccount(AccountDO accountDO);

    AccountDO lockById(String accountId);
}
