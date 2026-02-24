package com.alipay.alipay_plus.core.service.repository;

import com.alipay.alipay_plus.common.service.facade.request.*;
import com.alipay.alipay_plus.core.model.domain.AccountInfo;
import com.alipay.alipay_plus.core.model.domain.TransactionHistory;
import com.alipay.alipay_plus.core.model.domain.TransactionRecord;

import java.util.List;

public interface AccountRepository {
    AccountInfo queryAccountInfo(String accountNo);

    AccountInfo createAccount(CreateAccountRequest request);

    TransactionRecord queryTransactionRecord(QueryTransactionRecordRequest request);

    List<TransactionHistory> queryTransactionHistory(QueryTransactionHistoryRequest request);

    TransactionRecord insertTransactionRecord(InsertTransactionRecordRequest request);

    TransactionRecord updateTransactionRecord(UpdateTransactionRecordRequest request);

    AccountInfo lockById(String accountId);

    void updateAccountRecord(AccountInfo accountInfo);
}
