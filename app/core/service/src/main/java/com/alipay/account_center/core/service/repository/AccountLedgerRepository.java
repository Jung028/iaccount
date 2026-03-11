package com.alipay.account_center.core.service.repository;

import com.alipay.account_center.common.dal.auto.dataobject.LedgerEntryDO;
import com.alipay.account_center.common.service.facade.request.InsertLedgerRequest;


public interface AccountLedgerRepository {
    LedgerEntryDO insertLedger(InsertLedgerRequest insertLedgerRequest);
}
