package com.alipay.account_center.common.service.facade.request;

import com.alipay.account_center.common.service.facade.item.MetricCard;

/**
 * @author adam
 * @date 24/6/2026 9:51 PM
 */
public class QueryTransactionHistoryMerchantDashboard {

    private MetricCard<Integer> transactions;

    public MetricCard<Integer> getTransactions() {
        return transactions;
    }

    public void setTransactions(MetricCard<Integer> transactions) {
        this.transactions = transactions;
    }
}