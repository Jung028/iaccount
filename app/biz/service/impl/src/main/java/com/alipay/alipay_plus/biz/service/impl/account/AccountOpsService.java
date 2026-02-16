package com.alipay.alipay_plus.biz.service.impl.account;

public interface AccountOpsService {
    /**
     * Debit amount from account
     * @param debitAccountNo
     * @param amount
     * @param txnId
     */
    void debit(String debitAccountNo, String amount, String txnId);

    /**
     * Credit amount to the account
     * @param creditAccountNo
     * @param amount
     * @param txnId
     */
    void credit(String creditAccountNo, String amount, String txnId);

}
