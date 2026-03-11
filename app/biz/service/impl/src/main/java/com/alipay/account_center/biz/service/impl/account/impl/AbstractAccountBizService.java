package com.alipay.account_center.biz.service.impl.account.impl;

import com.alipay.account_center.biz.service.impl.template.AccountServiceTemplate;
import com.alipay.account_center.common.service.facade.constant.LoggerConstant;
import com.alipay.account_center.core.service.repository.AccountRepository;
import com.alipay.account_center.core.service.repository.AccountTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;

public abstract class AbstractAccountBizService {

    /**
     * logger
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(LoggerConstant.ACCOUNT_BIZ_SERVICE_LOG);

    /**
     * accountServiceTemplate
     */
    @Autowired
    protected AccountServiceTemplate accountServiceTemplate;


    /**
     * account repository
     */
    @Autowired
    protected AccountRepository accountRepository;

    /**
     * account transaction repository
     */
    @Autowired
    protected AccountTransactionRepository accountTransactionRepository;

    /**
     * transaction template
     */
    @Autowired
    protected TransactionTemplate transactionTemplate;

    /**
     * set accountServiceTemplate
     * @param accountServiceTemplate
     */
    public void setAccountServiceTemplate(AccountServiceTemplate accountServiceTemplate) {
        this.accountServiceTemplate = accountServiceTemplate;
    }

    /**
     * set account repository
     * @param accountRepository
     */
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * set account transaction repository
     * @param accountTransactionRepository
     */
    public void setAccountTransactionRepository(AccountTransactionRepository accountTransactionRepository) {
        this.accountTransactionRepository = accountTransactionRepository;
    }

    /**
     * set transaction template
     * @param transactionTemplate
     */
    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }
}
