package com.alipay.alipay_plus.core.service.repository.impl;

import com.alipay.alipay_plus.common.dal.auto.dataobject.AccountTransactionDO;
import com.alipay.alipay_plus.common.service.facade.request.InsertTransactionRecordRequest;
import com.alipay.alipay_plus.common.service.facade.request.QueryTransactionHistoryRequest;
import com.alipay.alipay_plus.common.service.facade.request.QueryTransactionRecordRequest;
import com.alipay.alipay_plus.common.service.facade.request.UpdateTransactionRecordRequest;
import com.alipay.alipay_plus.core.model.domain.TransactionHistory;
import com.alipay.alipay_plus.core.model.domain.TransactionRecord;
import com.alipay.alipay_plus.core.service.repository.AbstractDomainRepository;
import com.alipay.alipay_plus.core.service.repository.AccountTransactionRepository;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class AccountTransactionRepositoryImpl extends AbstractDomainRepository implements AccountTransactionRepository {


    @Override
    public TransactionRecord queryTransactionRecord(QueryTransactionRecordRequest request) {
        if (request == null) {
            return null;
        }
        AccountTransactionDO accountTransactionDO = accountTransactionDAO.queryTransactionRecord(request.getTxnId());
        return domainConverter.convertToModel(accountTransactionDO);
    }

    @Override
    public List<TransactionHistory> queryTransactionHistory(QueryTransactionHistoryRequest request) {
        if (request == null) {
            return Collections.emptyList();
        }
        List<AccountTransactionDO> transactionDOS = accountTransactionDAO.queryTransactionHistory(
                request.getTxnId(), request.getPageSize(), request.getPageNo());
        return domainConverter.convertToModelList(transactionDOS);
    }

    @Override
    public TransactionRecord insertTransactionRecord(InsertTransactionRecordRequest request) {
        if  (request == null) {
            return null;
        }
        AccountTransactionDO record = new AccountTransactionDO();
        record.setTxnId(request.getTxnId());
        record.setPayerAccountId(request.getPayerAccountNo());
        record.setPayeeAccountId(request.getPayeeAccountNo());
        //convert the MonetaryAmount to BigDecimal?
        BigDecimal dbAmount = request.getAmount().getNumber().numberValueExact(BigDecimal.class);
        record.setAmount(dbAmount);
        record.setCurrency(request.getAmount().getCurrency().getCurrencyCode());
        record.setStatus(request.getStatus().getCode());

        AccountTransactionDO transactionRecordDO = accountTransactionDAO.insertTransactionRecord(record);

        return domainConverter.convertToModel(transactionRecordDO);
    }

    @Override
    public TransactionRecord updateTransactionRecord(UpdateTransactionRecordRequest request) {
        if (request == null) {
            return null;
        }
        AccountTransactionDO record = new AccountTransactionDO();
        record.setTxnId(request.getTxnId());
        record.setStatus(request.getStatus());
        AccountTransactionDO accountTransactionDO = accountTransactionDAO.updateTransactionRecord(record);
        return domainConverter.convertToModel(accountTransactionDO);
    }
}
