package com.alipay.account_center.core.service.repository.impl;

import com.alipay.account_center.common.dal.auto.dataobject.AccountDO;
import com.alipay.account_center.common.service.facade.request.CreateAccountRequest;
import com.alipay.account_center.core.model.converter.DomainConverter;
import com.alipay.account_center.core.model.domain.AccountInfo;
import com.alipay.account_center.core.model.enums.AccountStatusEnum;
import com.alipay.account_center.core.service.repository.AbstractDomainRepository;
import com.alipay.account_center.core.service.repository.AccountRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class AccountRepositoryImpl extends AbstractDomainRepository implements AccountRepository {

    @Override
    public AccountInfo queryAccountInfo(String accountNo) {
        if (accountNo.isBlank()) {
            return null;
        }
        AccountDO accountDO = accountDAO.queryAccountInfo(accountNo);
        return DomainConverter.convertToModel(accountDO);
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
        accountDO.setAccountRelationId(request.getAccRelationId());
        accountDO.setCurrency(request.getCurrency());
        accountDO.setBalance(request.getBalance());
        accountDO.setStatus(AccountStatusEnum.ACTIVE.getCode());
        accountDO.setExtInfo(request.getExtInfo());
        AccountDO accountResult = accountDAO.createAccount(accountDO);
        return DomainConverter.convertToModel(accountResult);
    }

    @Override
    public AccountInfo lockById(String accountId) {
        if (accountId.isBlank()) {
            return new AccountInfo();
        }
        AccountDO accountDO = accountDAO.lockById(accountId);
        return DomainConverter.convertToModel(accountDO);
    }

    @Override
    public void updateAccountRecord(AccountInfo accountInfo) {
        if (accountInfo == null) {

        }
    }
}
