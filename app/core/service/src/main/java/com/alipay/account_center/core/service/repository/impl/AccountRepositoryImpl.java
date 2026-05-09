package com.alipay.account_center.core.service.repository.impl;

import com.alipay.account_center.common.dal.auto.dataobject.AccountDO;
import com.alipay.account_center.common.service.facade.request.CreateAccountRequest;
import com.alipay.account_center.core.model.converter.DomainConverter;
import com.alipay.account_center.core.model.domain.AccountInfo;
import com.alipay.account_center.core.model.enums.AccountStatusEnum;
import com.alipay.account_center.core.model.exception.RepositoryException;
import com.alipay.account_center.core.service.repository.AbstractDomainRepository;
import com.alipay.account_center.core.service.repository.AccountRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;
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
    public AccountInfo queryAccountInfoByName(String accountName) {
        if (accountName.isBlank()) {
            return null;
        }
        AccountDO accountDO = accountDAO.queryAccountInfoByName(accountName);
        return DomainConverter.convertToModel(accountDO);    }

    @Override
    public AccountInfo queryAccountInfoByUserId(String userId) {
        if (userId.isBlank()) {
            return null;
        }
        AccountDO accountDO = accountDAO.queryAccountInfoByUserId(userId);
        return DomainConverter.convertToModel(accountDO);
    }

    @Override
    public AccountInfo createAccount(CreateAccountRequest request) {
        if (request == null) {
            return null;
        }
        try {
            AccountDO accountDO = new AccountDO();
            accountDO.setAccountId(UUID.randomUUID().toString());
            accountDO.setAccountNumber("ACC_" + request.getUserId().substring(0, 4) + String.format("%06d", new Random().nextInt(999999)));
            accountDO.setAccountName(request.getAccountName()); // savings
            accountDO.setAccountType(request.getAccountType().getCode()); // by default is savings account,
            accountDO.setAccountRelationId(request.getUserId());
            accountDO.setCurrency(request.getCurrency());
            accountDO.setBalance(new BigDecimal(0));
            accountDO.setStatus(AccountStatusEnum.ACTIVE.getCode());
            accountDO.setGmtModified(new Date());
            accountDO.setGmtCreate(new Date());
            accountDO.setOwnerType(request.getOwnerType());

            int rows = accountDAO.createAccount(accountDO);
            if (rows <= 0) {
                throw new RepositoryException("Insert failed for accountId: " + accountDO.getAccountId());
            }
            return DomainConverter.convertToModel(accountDO);
        } catch (RepositoryException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("DB error during create account", e);
        }
    }

    @Override
    public AccountInfo lockById(String accountId) {
        if (accountId == null || accountId.isBlank()) { // same fix as queryAccountInfo
            return null;
        }
        AccountDO accountDO = accountDAO.lockById(accountId);
        return DomainConverter.convertToModel(accountDO);
    }

    @Override
    public int updateAccountRecord(AccountInfo accountInfo) {
        if (accountInfo == null) {
            return 0;
        }
        try {
            AccountDO accountDO = DomainConverter.convertToDomain(accountInfo);
            int rows = accountDAO.updateAccount(accountDO);
            if (rows <= 0) {
                throw new RepositoryException("Update affected 0 rows for accountId: " + accountInfo.getAccountId());
            }
            return rows;
        } catch (RepositoryException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("DB error during update account", e);
        }
    }
}
