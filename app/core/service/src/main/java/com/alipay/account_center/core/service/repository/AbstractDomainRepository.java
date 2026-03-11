package com.alipay.account_center.core.service.repository;

import com.alipay.account_center.common.dal.auto.custom.AccountDAO;
import com.alipay.account_center.common.dal.auto.custom.AccountTransactionDAO;
import com.alipay.account_center.core.model.converter.DomainConverter;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractDomainRepository {

    @Autowired
    protected AccountDAO accountDAO;

    @Autowired
    protected AccountTransactionDAO accountTransactionDAO;

}
