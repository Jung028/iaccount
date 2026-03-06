package com.alipay.alipay_plus.core.service.repository;

import com.alipay.account.common.dal.auto.dataobject.LedgerEntryDO;
import com.alipay.alipay_plus.common.service.facade.request.InsertLedgerRequest;

public interface AccountLedgerRepository {
    LedgerEntryDO insertLedger(InsertLedgerRequest insertLedgerRequest);
}
