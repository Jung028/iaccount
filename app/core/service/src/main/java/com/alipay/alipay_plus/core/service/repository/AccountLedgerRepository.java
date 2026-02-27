package com.alipay.alipay_plus.core.service.repository;

import com.alipay.alipay_plus.biz.service.impl.request.InsertLedgerRequest;
import com.alipay.alipay_plus.common.dal.auto.dataobject.LedgerEntryDO;

public interface AccountLedgerRepository {
    LedgerEntryDO insertLedger(InsertLedgerRequest insertLedgerRequest);
}
