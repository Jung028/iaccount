package com.alipay.alipay_plus.core.service.repository.impl;

import com.alipay.alipay_plus.common.dal.auto.dataobject.AccountDO;
import com.alipay.alipay_plus.common.dal.auto.dataobject.AccountTransactionDO;
import com.alipay.alipay_plus.common.service.facade.request.QueryTransactionHistoryRequest;
import com.alipay.alipay_plus.common.service.facade.request.QueryTransactionRecordRequest;
import com.alipay.alipay_plus.core.model.domain.AccountInfo;
import com.alipay.alipay_plus.core.model.domain.TransactionHistory;
import com.alipay.alipay_plus.core.model.domain.TransactionRecord;
import com.alipay.alipay_plus.core.service.repository.AbstractDomainRepository;
import com.alipay.alipay_plus.core.service.repository.AccountRepository;

import java.util.Collections;
import java.util.List;


public class AccountRepositoryImpl extends AbstractDomainRepository implements AccountRepository {

    @Override
    public AccountInfo queryAccountInfo(String accountNo) {
        if (accountNo.isBlank()) {
            return null;
        }
        AccountDO accountDO = accountDAO.queryAccountInfo(accountNo);
        return domainConverter.convertToModel(accountDO);
    }

    @Override
    public TransactionRecord queryTransactionRecord(QueryTransactionRecordRequest request) {
        if (request == null) {
            return null;
        }
        AccountTransactionDO accountTransactionDO = accountDAO.queryTransactionRecord(request.getTxnId());
        return domainConverter.convertToModel(accountTransactionDO);
    }

    @Override
    public List<TransactionHistory> queryTransactionHistory(QueryTransactionHistoryRequest request) {
        if (request == null) {
            return Collections.emptyList();
        }
        List<AccountTransactionDO> transactionDOS = accountDAO.queryTransactionHistory(
                request.getTxnId(), request.getPageSize(), request.getPageNo());
        return domainConverter.convertToModelList(transactionDOS);
    }
}
