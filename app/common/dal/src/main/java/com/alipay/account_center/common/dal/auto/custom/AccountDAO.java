package com.alipay.account_center.common.dal.auto.custom;

import com.alipay.account_center.common.dal.auto.dataobject.AccountDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AccountDAO {
    AccountDO queryAccountInfo(@Param("accountId") String accountId);

    AccountDO queryAccountInfoByUserId(@Param("userId") String userId);

    int createAccount(AccountDO accountDO);

    AccountDO lockById(@Param("accountId") String accountId);

    int updateAccount(AccountDO accountDO);
}
