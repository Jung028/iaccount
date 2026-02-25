package com.alipay.alipay_plus.biz.service.impl.account.impl;

import com.alipay.alipay_plus.biz.service.impl.account.AccountOpsService;
import com.alipay.alipay_plus.biz.service.impl.template.AccountServiceTemplate;
import com.alipay.alipay_plus.common.service.facade.constant.LoggerConstant;
import com.alipay.alipay_plus.core.service.repository.AccountRepository;
import com.alipay.alipay_plus.core.service.repository.AccountTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionTemplate;

public abstract class AbstractAccountBizService {

    /**
     * logger
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(LoggerConstant.ACCOUNT_BIZ_SERVICE_LOG);

    /**
     * accountServiceTemplate
     */
    protected AccountServiceTemplate accountServiceTemplate;

    /**
     * accountOpsService
     */
    protected AccountOpsService accountOpsService;

    /**
     * account repository
     */
    protected AccountRepository accountRepository;

    /**
     * account transaction repository
     */
    protected AccountTransactionRepository accountTransactionRepository;

    /**
     * transaction template
     */
    protected TransactionTemplate transactionTemplate;

    /**
     * set accountServiceTemplate
     * @param accountServiceTemplate
     */
    public void setAccountServiceTemplate(AccountServiceTemplate accountServiceTemplate) {
        this.accountServiceTemplate = accountServiceTemplate;
    }

    /**
     * set accountOpsService
     * @param accountOpsService
     */
    public void setAccountOpsService(AccountOpsService accountOpsService) {
        this.accountOpsService = accountOpsService;
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
