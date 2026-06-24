package com.alipay.account_center.common.dal.auto.custom;


import com.alipay.account_center.common.dal.auto.dataobject.TransactionDO;
import com.alipay.account_center.common.dal.auto.dataobject.TransactionMetricsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.Date;
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

    int queryTransactionTotalCount(@Param("accountId") String accountId);

    List<TransactionDO> queryTransactionHistory(@Param("accountId") String accountId,
                                                @Param("pageSize") int pageSize,
                                                @Param("pageNo") int pageNo,
                                                @Param("payerAccountId") String payerAccountId,
                                                @Param("gmtCreate") LocalDateTime gmtCreate,
                                                @Param("gmtComplete") LocalDateTime gmtComplete,
                                                @Param("amountMax") int amountMax,
                                                @Param("amountMin") int amountMin,
                                                @Param("txnCategory") String txnCategory,
                                                @Param("txnType") String txnType,
                                                @Param("txnStatus") String txnStatus);


    List<TransactionDO> queryCustomerTransactionHistoryByMerchantId(@Param("accountId") String accountId,
                                                                    @Param("pageSize") int pageSize,
                                                                    @Param("pageNo") int pageNo,
                                                                    @Param("gmtCreate") LocalDateTime gmtCreate,
                                                                    @Param("gmtCompleted") LocalDateTime gmtCompleted,
                                                                    @Param("amountMax") int amountMax,
                                                                    @Param("amountMin") int amountMin,
                                                                    @Param("txnCategory") String txnCategory,
                                                                    @Param("txnType") String txnType,
                                                                    @Param("txnStatus") String txnStatus);

    int queryTotalRevenue(@Param("merchantId") String merchantId,
                          @Param("gmtCreateFrom") LocalDateTime gmtCreateFrom,
                          @Param("gmtCreateTo") LocalDateTime gmtCreateTo);

    double queryAverageBasket(@Param("merchantId") String merchantId,
                              @Param("gmtCreateFrom") LocalDateTime gmtCreateFrom,
                              @Param("gmtCreateTo") LocalDateTime gmtCreateTo);

    int queryNewCustomerCount(@Param("merchantId") String merchantId,
                              @Param("gmtCreateFrom") LocalDateTime gmtCreateFrom,
                              @Param("gmtCreateTo") LocalDateTime gmtCreateTo);
}