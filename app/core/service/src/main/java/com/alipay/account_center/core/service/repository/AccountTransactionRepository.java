package com.alipay.account_center.core.service.repository;

import com.alipay.account_center.common.service.facade.request.InsertTransactionRecordRequest;
import com.alipay.account_center.common.service.facade.request.QueryTransactionHistoryRequest;
import com.alipay.account_center.common.service.facade.request.QueryTransactionRecordRequest;
import com.alipay.account_center.common.service.facade.request.UpdateTransactionRecordRequest;
import com.alipay.account_center.core.model.domain.TransactionHistory;
import com.alipay.account_center.core.model.domain.TransactionRecord;

import java.util.List;

public interface AccountTransactionRepository {
    TransactionRecord queryTransactionRecord(QueryTransactionRecordRequest request);

    List<TransactionHistory> queryTransactionHistory(QueryTransactionHistoryRequest request);

    TransactionRecord insertTransactionRecord(InsertTransactionRecordRequest request);

    int updateTransactionRecord(UpdateTransactionRecordRequest request);

}
