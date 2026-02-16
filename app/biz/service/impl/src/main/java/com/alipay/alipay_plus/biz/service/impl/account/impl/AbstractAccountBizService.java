package com.alipay.alipay_plus.biz.service.impl.account.impl;

import com.alipay.alipay_plus.biz.service.impl.account.AccountOpsService;
import com.alipay.alipay_plus.biz.service.impl.template.AccountServiceTemplate;
import com.alipay.alipay_plus.common.service.facade.constant.LoggerConstant;
import com.alipay.alipay_plus.core.service.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}
