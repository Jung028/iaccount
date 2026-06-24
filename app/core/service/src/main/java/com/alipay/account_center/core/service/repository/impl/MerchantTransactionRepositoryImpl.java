package com.alipay.account_center.core.service.repository.impl;

import com.alipay.account_center.common.dal.auto.dataobject.TransactionDO;
import com.alipay.account_center.common.service.facade.request.QueryTotalRevenueRequest;
import com.alipay.account_center.common.service.facade.request.QueryTransactionHistoryRequest;
import com.alipay.account_center.core.model.converter.DomainConverter;
import com.alipay.account_center.core.model.domain.TransactionHistory;
import com.alipay.account_center.core.model.exception.RepositoryException;
import com.alipay.account_center.core.service.repository.AbstractDomainRepository;
import com.alipay.account_center.core.service.repository.MerchantTransactionRepository;
import com.alipay.common.tracer.core.utils.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author adam
 * @date 23/6/2026 11:13 PM
 */
public class MerchantTransactionRepositoryImpl extends AbstractDomainRepository implements MerchantTransactionRepository {

    @Override
    public int queryTotalRevenue(QueryTotalRevenueRequest request) {
        if (request == null || StringUtils.isBlank(request.getMerchantId())) {
            return 0;
        }
        try {
            return accountTransactionDAO.queryTotalRevenue(
                    request.getMerchantId(),
                    request.getGmtCreateFrom(),
                    request.getGmtCreateTo()
            );
        } catch (RepositoryException e) {
            throw e;
        }
    }

    @Override
    public double queryAverageBasket(String merchantId, LocalDateTime from, LocalDateTime to) {
        if (StringUtils.isBlank(merchantId)) {
            return 0;
        }
        try {
            return accountTransactionDAO.queryAverageBasket(merchantId, from, to);
        } catch (RepositoryException e) {
            throw e;
        }
    }

    @Override
    public int queryNewCustomerCount(String merchantId, LocalDateTime from, LocalDateTime to) {
        if (StringUtils.isBlank(merchantId)) {
            return 0;
        }
        try {
            return accountTransactionDAO.queryNewCustomerCount(merchantId, from, to);
        } catch (RepositoryException e) {
            throw e;
        }
    }

    @Override
    public List<TransactionHistory> queryCustomerTransactionHistoryByMerchantId(QueryTransactionHistoryRequest request) {
        if (request == null) {
            return Collections.emptyList();
        }
        try {
            List<TransactionDO> transactionDOS = accountTransactionDAO.queryCustomerTransactionHistoryByMerchantId(
                    request.getAccountId(), request.getPageSize(), request.getPageNo(),
                    request.getGmtCreate(), request.getAmountMax(), request.getAmountMin(),
                    request.getTxnCategory(), request.getTxnType(), request.getTxnStatus());
            return DomainConverter.convertToModelList(transactionDOS);
        } catch (RepositoryException e) {
            throw e;
        }
    }
}
