package com.alipay.account_center.core.service.repository;

import com.alipay.account_center.common.service.facade.request.QueryAverageBasketRequest;
import com.alipay.account_center.common.service.facade.request.QueryNewCustomerCountRequest;
import com.alipay.account_center.common.service.facade.request.QueryTotalRevenueRequest;
import com.alipay.account_center.common.service.facade.request.QueryTransactionHistoryRequest;
import com.alipay.account_center.core.model.domain.TransactionHistory;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author adam
 * @date 23/6/2026 11:03 PM
 */
public interface MerchantTransactionRepository {
    int queryTotalRevenue(String merchantId, LocalDateTime from, LocalDateTime to);

    double queryAverageBasket(String merchantId, LocalDateTime from, LocalDateTime to);

    int queryNewCustomerCount(String merchantId, LocalDateTime from, LocalDateTime to);

    List<TransactionHistory> queryCustomerTransactionHistoryByMerchantId(QueryTransactionHistoryRequest request, LocalDateTime from, LocalDateTime to);
}
