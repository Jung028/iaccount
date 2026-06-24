package com.alipay.account_center.biz.service.impl.account.impl;

import com.alipay.account_center.biz.service.impl.lock.DistributedLock;
import com.alipay.account_center.biz.service.impl.template.AccountServiceTemplate;
import com.alipay.account_center.common.service.facade.constant.LoggerConstant;
import com.alipay.account_center.common.service.integration.user.TopUpServiceClient;
import com.alipay.account_center.common.service.integration.wallet.WalletServiceClient;
import com.alipay.account_center.core.service.repository.AccountLedgerRepository;
import com.alipay.account_center.core.service.repository.AccountRepository;
import com.alipay.account_center.core.service.repository.AccountTransactionRepository;
import com.alipay.account_center.core.service.repository.MerchantTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
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
     * merchant transaction repository
     */
    @Autowired
    protected MerchantTransactionRepository merchantTransactionRepository;

    /**
     * transaction template
     */
    @Autowired
    protected TransactionTemplate transactionTemplate;

    /**
     * account ledger repository
     */
    @Autowired
    protected AccountLedgerRepository accountLedgerRepository;

    /**
     * distributed lock
     */
    @Autowired
    protected DistributedLock distributedLock;

    /**
     * top up service client
     */
    @Autowired
    protected TopUpServiceClient topUpServiceClient;

    /**
     * kafka template
     */
    @Autowired
    protected KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * wallet service client
     */
    @Autowired
    protected WalletServiceClient walletServiceClient;

}
