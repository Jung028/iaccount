package com.alipay.account_center.common.dal.auto.custom;

import com.alipay.account_center.common.dal.auto.dataobject.AccountDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountDAO {
    AccountDO queryAccountInfo(String accountId);

    AccountDO createAccount(AccountDO accountDO);

    AccountDO lockById(String accountId);
}
