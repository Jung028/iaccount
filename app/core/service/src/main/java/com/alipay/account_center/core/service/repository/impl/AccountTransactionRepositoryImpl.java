package com.alipay.account_center.core.service.repository.impl;

import com.alipay.account_center.common.dal.auto.dataobject.TransactionDO;
import com.alipay.account_center.common.service.facade.request.InsertTransactionRecordRequest;
import com.alipay.account_center.common.service.facade.request.QueryTransactionHistoryRequest;
import com.alipay.account_center.common.service.facade.request.QueryTransactionRecordRequest;
import com.alipay.account_center.common.service.facade.request.UpdateTransactionRecordRequest;
import com.alipay.account_center.core.model.converter.DomainConverter;
import com.alipay.account_center.core.model.domain.TransactionHistory;
import com.alipay.account_center.core.model.domain.TransactionRecord;
import com.alipay.account_center.core.model.exception.RepositoryException;
import com.alipay.account_center.core.service.repository.AbstractDomainRepository;
import com.alipay.account_center.core.service.repository.AccountTransactionRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    public TransactionRecord queryTransactionByStatus(QueryTransactionRecordRequest request) {
        if (request == null) {
            return null;
        }
        TransactionDO transactionDO = accountTransactionDAO.queryTransactionByStatus(request.getAccountId(), request.getTxnStatusList());
        return DomainConverter.convertToModel(transactionDO);
    }

    @Override
    public List<TransactionHistory> queryTransactionHistory(QueryTransactionHistoryRequest request) {
        if (request == null) {
            return Collections.emptyList();
        }
        List<TransactionDO> transactionDOS = accountTransactionDAO.queryTransactionHistory (
                request.getAccountId(), request.getPageSize(), request.getPageNo(), request.getPayerAccountId(),
                request.getGmtCreate(), request.getAmountMax(), request.getAmountMin(), request.getTxnCategory(),
                request.getTxnType(), request.getTxnStatus());
        return DomainConverter.convertToModelList(transactionDOS);
    }

    @Override
    public TransactionRecord insertTransactionRecord(InsertTransactionRecordRequest request) {
        if (request == null) {
            return null;
        }
        try {
            TransactionDO record = new TransactionDO();
            // generate random UUID
            record.setTxnId(UUID.randomUUID().toString());
            record.setPayerAccountId(request.getPayerAccountNo());
            record.setPayeeAccountId(request.getPayeeAccountNo());
            record.setAmount(request.getAmount());
            record.setCurrency(request.getCurrency());
            record.setStatus(request.getStatus().getCode());
            record.setGmtCreate(new Date());
            record.setGmtModified(new Date());
            record.setGmtComplete(new Date());
            System.out.println("HELLO HERE" + request.getTxnType().getCode());
            record.setType(request.getTxnType().getCode());

            int rows = accountTransactionDAO.insertTransactionRecord(record); // DAO returns int
            if (rows <= 0) {
                throw new RepositoryException("Insert failed for txnId: " + request.getTxnId());
            }
            return DomainConverter.convertToModel(record);
        } catch (RepositoryException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("DB error during insert", e);
        }
    }

    @Override
    public int updateTransactionRecord(UpdateTransactionRecordRequest request) {
        if (request == null) return 0;
        try {
            TransactionDO record = new TransactionDO();
            record.setTxnId(request.getTxnId());
            record.setStatus(request.getStatus());
            record.setFailureReason(request.getFailReason());

            int rows = accountTransactionDAO.updateTransactionRecord(record);
            if (rows <= 0) {
                throw new RepositoryException("Update affected 0 rows for txnId: " + request.getTxnId());
            }
            return rows;
        } catch (RepositoryException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("DB error during update", e);
        }
    }

    @Override
    public int queryTransactionTotalCount(QueryTransactionHistoryRequest request) {
        if (request == null) {
            return 0;
        }
        return accountTransactionDAO.queryTransactionTotalCount(request.getAccountId());

    }

}
