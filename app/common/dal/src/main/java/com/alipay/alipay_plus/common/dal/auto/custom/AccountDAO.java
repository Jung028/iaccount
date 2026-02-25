package com.alipay.alipay_plus.common.dal.auto.custom;

import com.alipay.alipay_plus.common.dal.auto.dataobject.AccountDO;
import com.alipay.alipay_plus.common.dal.auto.dataobject.AccountTransactionDO;

import java.util.List;

public interface AccountDAO {
    AccountDO queryAccountInfo(String accountId);

    AccountDO createAccount(AccountDO accountDO);

    AccountDO lockById(String accountId);
}
