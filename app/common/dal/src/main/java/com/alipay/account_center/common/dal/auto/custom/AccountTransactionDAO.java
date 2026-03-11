package com.alipay.account_center.common.dal.auto.custom;


import com.alipay.account_center.common.dal.auto.dataobject.AccountTransactionDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author jung
 * @date 2026-02-14 16:46:38
 */
@Mapper
public interface AccountTransactionDAO {

    AccountTransactionDO queryTransactionRecord(String txnId, String transactionId);

    List<AccountTransactionDO> queryTransactionHistory(String txnId, String pageSize, String offSet);

    AccountTransactionDO insertTransactionRecord(AccountTransactionDO accountTransactionDO);

    AccountTransactionDO updateTransactionRecord(AccountTransactionDO record);

}