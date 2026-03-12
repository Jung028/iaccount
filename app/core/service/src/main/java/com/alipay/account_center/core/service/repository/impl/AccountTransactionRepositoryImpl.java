package com.alipay.account_center.core.service.repository.impl;

import com.alipay.account_center.common.dal.auto.dataobject.TransactionDO;
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
        TransactionDO transactionDO = accountTransactionDAO.queryTransactionRecord(request.getTxnId(), request.getAccountId());
        return DomainConverter.convertToModel(transactionDO);
    }

    @Override
    public List<TransactionHistory> queryTransactionHistory(QueryTransactionHistoryRequest request) {
        if (request == null) {
            return Collections.emptyList();
        }
        List<TransactionDO> transactionDOS = accountTransactionDAO.queryTransactionHistory(
                request.getTxnId(), request.getPageSize(), request.getPageNo());
        return DomainConverter.convertToModelList(transactionDOS);
    }

    @Override
    public TransactionRecord insertTransactionRecord(InsertTransactionRecordRequest request) {
        if  (request == null) {
            return null;
        }
        TransactionDO record = new TransactionDO();
        record.setTxnId(request.getTxnId());
        record.setPayerAccountId(request.getPayerAccountNo());
        record.setPayeeAccountId(request.getPayeeAccountNo());
        //convert the MonetaryAmount to BigDecimal?
        BigDecimal dbAmount = request.getAmount();
        record.setAmount(dbAmount);
        record.setCurrency(request.getCurrency().getCurrencyCode());
        record.setStatus(request.getStatus().getCode());

        TransactionDO transactionRecordDO = accountTransactionDAO.insertTransactionRecord(record);

        return DomainConverter.convertToModel(transactionRecordDO);
    }

    @Override
    public void updateTransactionRecord(UpdateTransactionRecordRequest request) {
        if (request == null) {
            return;
        }
        TransactionDO record = new TransactionDO();
        record.setTxnId(request.getTxnId());
        record.setStatus(request.getStatus());
        accountTransactionDAO.updateTransactionRecord(record);

    }
}
