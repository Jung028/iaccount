package com.alipay.account_center.common.dal.auto.custom;


import com.alipay.account_center.common.dal.auto.dataobject.TransactionDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author jung
 * @date 2026-02-14 16:46:38
 */
@Mapper
public interface TransactionDAO {

    TransactionDO queryTransactionRecord(@Param("txnId") String txnId, @Param("accountId") String accountId);

    List<TransactionDO> queryTransactionHistory(@Param("txnId") String txnId, @Param("pageSize") String pageSize, @Param("pageNo") String pageNo);

    int insertTransactionRecord(TransactionDO transactionDO);

    int updateTransactionRecord(TransactionDO record);

}