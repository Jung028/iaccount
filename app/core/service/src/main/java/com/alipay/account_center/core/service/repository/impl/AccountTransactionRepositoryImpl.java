package com.alipay.account_center.core.service.repository.impl;

import com.alipay.account_center.common.dal.auto.dataobject.AccountTransactionDO;
import com.alipay.account_center.common.service.facade.request.InsertTransactionRecordRequest;
import com.alipay.account_center.common.service.facade.request.QueryTransactionHistoryRequest;
import com.alipay.account_center.common.service.facade.request.QueryTransactionRecordRequest;
import com.alipay.account_center.common.service.facade.request.UpdateTransactionRecordRequest;
import com.alipay.account_center.core.model.converter.DomainConverter;
import com.alipay.account_center.core.model.domain.TransactionHistory;
import com.alipay.account_center.core.model.domain.TransactionRecord;
import com.alipay.account_center.core.service.repository.AbstractDomainRepository;
import com.alipay.account_center.core.service.repository.AccountTransactionRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Repository
public class AccountTransactionRepositoryImpl extends AbstractDomainRepository implements AccountTransactionRepository {


    @Override
    public TransactionRecord queryTransactionRecord(QueryTransactionRecordRequest request) {
        if (request == null) {
            return null;
        }
        AccountTransactionDO accountTransactionDO = accountTransactionDAO.queryTransactionRecord(request.getTxnId(), request.getTxnId());
        return DomainConverter.convertToModel(accountTransactionDO);
    }

    @Override
    public List<TransactionHistory> queryTransactionHistory(QueryTransactionHistoryRequest request) {
        if (request == null) {
            return Collections.emptyList();
        }
        List<AccountTransactionDO> transactionDOS = accountTransactionDAO.queryTransactionHistory(
                request.getTxnId(), request.getPageSize(), request.getPageNo());
        return DomainConverter.convertToModelList(transactionDOS);
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

        return DomainConverter.convertToModel(transactionRecordDO);
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
        return DomainConverter.convertToModel(accountTransactionDO);
    }
}
