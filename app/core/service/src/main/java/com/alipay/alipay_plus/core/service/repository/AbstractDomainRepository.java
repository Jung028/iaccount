package com.alipay.alipay_plus.core.service.repository;

import com.alipay.alipay_plus.common.dal.auto.custom.AccountDAO;
import com.alipay.alipay_plus.common.dal.auto.custom.AccountTransactionDAO;
import com.alipay.alipay_plus.core.model.converter.DomainConverter;

public class AbstractDomainRepository {
    protected AccountRepository accountRepository;

    protected DomainConverter domainConverter;

    protected AccountDAO accountDAO;

    protected AccountTransactionDAO accountTransactionDAO;

    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void setDomainConverter(DomainConverter domainConverter) {
        this.domainConverter = domainConverter;
    }

    public void setAccountDAO(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public void setAccountTransactionDAO(AccountTransactionDAO accountTransactionDAO) {
        this.accountTransactionDAO = accountTransactionDAO;
    }
}
