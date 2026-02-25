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
