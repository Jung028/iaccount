package com.alipay.alipay_plus.biz.service.impl.account.impl;

import com.alipay.alipay_plus.biz.service.impl.account.AccountOpsService;

public class AccountOpsServiceImpl implements AccountOpsService {
    @Override
    public void debit(String debitAccountNo, String amount, String txnId) {
        // TODO : add amount into debigAccountNo
    }

    @Override
    public void credit(String creditAccountNo, String amount, String txnId) {
        // check if amount is sufficient
        // TODO : deduct amount from creditAccountNo
    }
}
