package com.alipay.account_center.common.service.facade.request;

import com.alipay.account_center.common.service.facade.baseresult.AccountPageBaseResult;
import com.alipay.account_center.common.service.facade.item.TransactionHistoryItem;

import java.util.List;

/**
 * @author adam
 * @date 15/3/2026 12:42 AM
 */
public class QueryTransactionHistoryResult extends AccountPageBaseResult {
    private List<TransactionHistoryItem> transactionHistoryList;

    public List<TransactionHistoryItem> getTransactionHistoryList() {
        return transactionHistoryList;
    }

    public void setTransactionHistoryList(List<TransactionHistoryItem> transactionHistoryList) {
        this.transactionHistoryList = transactionHistoryList;
    }
}