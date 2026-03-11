package com.alipay.account_center.core.service.repository.impl;

import com.alipay.account_center.common.dal.auto.dataobject.LedgerEntryDO;
import com.alipay.account_center.common.service.facade.request.InsertLedgerRequest;
import com.alipay.account_center.core.service.repository.AccountLedgerRepository;
import org.springframework.stereotype.Repository;

@Repository
public class AccountLedgerRepositoryImpl implements AccountLedgerRepository {
    @Override
    public LedgerEntryDO insertLedger(InsertLedgerRequest insertLedgerRequest) {
        return null;
    }
}
