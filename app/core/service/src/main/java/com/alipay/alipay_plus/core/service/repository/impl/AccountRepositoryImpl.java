package com.alipay.alipay_plus.core.service.repository.impl;

import com.alipay.alipay_plus.common.dal.auto.dataobject.AccountDO;
import com.alipay.alipay_plus.common.dal.auto.dataobject.AccountTransactionDO;
import com.alipay.alipay_plus.common.service.facade.request.*;
import com.alipay.alipay_plus.core.model.domain.AccountInfo;
import com.alipay.alipay_plus.core.model.domain.TransactionHistory;
import com.alipay.alipay_plus.core.model.domain.TransactionRecord;
import com.alipay.alipay_plus.core.model.enums.AccountStatusEnum;
import com.alipay.alipay_plus.core.service.repository.AbstractDomainRepository;
import com.alipay.alipay_plus.core.service.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


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
    public AccountInfo createAccount(CreateAccountRequest request) {
        if (request == null) {
            return new AccountInfo();
        }
        AccountDO accountDO = new AccountDO();
        accountDO.setAccountId(UUID.randomUUID().toString());
        accountDO.setAccountName(request.getAccountName());
        accountDO.setAccountType(request.getAccountType());
        accountDO.setAccRelationId(request.getAccRelationId());
        accountDO.setCurrency(request.getCurrency());
        accountDO.setBalance(request.getBalance());
        accountDO.setStatus(AccountStatusEnum.ACTIVE.getCode());
        accountDO.setDesc(request.getDesc());
        AccountDO accountResult = accountDAO.createAccount(accountDO);
        return domainConverter.convertToModel(accountResult);
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

        AccountTransactionDO transactionRecordDO = accountDAO.insertTransactionRecord(record);

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
        AccountTransactionDO accountTransactionDO = accountDAO.updateTransactionRecord(record);
        return domainConverter.convertToModel(accountTransactionDO);
    }

    @Override
    public AccountInfo lockById(String accountId) {
        if (accountId.isBlank()) {
            return new AccountInfo();
        }
        AccountDO accountDO = accountDAO.lockById(accountId);
        return domainConverter.convertToModel(accountDO);
    }

    @Override
    public void updateAccountRecord(AccountInfo accountInfo) {
        if (accountInfo == null) {

        }
    }
}
