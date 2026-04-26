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

    TransactionDO queryIncompleteTransactionRecord(@Param("txnId") String txnId);

    TransactionDO queryTransactionRecord(@Param("txnId") String txnId, @Param("accountId") String accountId);

    TransactionDO queryTransactionByStatus(@Param("accountId") String accountId, @Param("statusList") List<String> statusList);

    int insertTransactionRecord(TransactionDO transactionDO);

    int updateTransactionRecord(TransactionDO record);

    int queryTransactionTotalCount(String accountId);

    List<TransactionDO> queryTransactionHistory(@Param("accountId") String accountId, @Param("pageSize") int pageSize, @Param("pageNo") int pageNo,
                                                @Param("payerAccountId") String payerAccountId, @Param("gmtCreate") String gmtCreate);
}