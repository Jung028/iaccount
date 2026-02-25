package com.alipay.alipay_plus.core.service.repository;

import com.alipay.alipay_plus.common.service.facade.request.InsertTransactionRecordRequest;
import com.alipay.alipay_plus.common.service.facade.request.QueryTransactionHistoryRequest;
import com.alipay.alipay_plus.common.service.facade.request.QueryTransactionRecordRequest;
import com.alipay.alipay_plus.common.service.facade.request.UpdateTransactionRecordRequest;
import com.alipay.alipay_plus.core.model.domain.TransactionHistory;
import com.alipay.alipay_plus.core.model.domain.TransactionRecord;

import java.util.List;

public interface AccountTransactionRepository {
    TransactionRecord queryTransactionRecord(QueryTransactionRecordRequest request);

    List<TransactionHistory> queryTransactionHistory(QueryTransactionHistoryRequest request);

    TransactionRecord insertTransactionRecord(InsertTransactionRecordRequest request);

    TransactionRecord updateTransactionRecord(UpdateTransactionRecordRequest request);

}
