package com.alipay.account_center.core.service.repository;

import com.alipay.account_center.common.dal.auto.custom.AccountDAO;
import com.alipay.account_center.common.dal.auto.custom.LedgerEntryDAO;
import com.alipay.account_center.common.dal.auto.custom.TransactionDAO;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractDomainRepository {

    @Autowired
    protected AccountDAO accountDAO;

    @Autowired
    protected TransactionDAO accountTransactionDAO;

    @Autowired
    protected LedgerEntryDAO ledgerEntryDAO;
}
