package com.alipay.account_center.core.model.domain.pair;

import com.alipay.account_center.core.model.domain.AccountInfo;

/**
 * @author adam
 * @date 7/4/2026 10:15 PM
 */
public class AccountPair {
    private AccountInfo payer;
    private AccountInfo payee;

    public AccountPair(AccountInfo payer, AccountInfo payee) {
        this.payer = payer;
        this.payee = payee;
    }

    public AccountInfo getPayee() {
        return payee;
    }

    public void setPayee(AccountInfo payee) {
        this.payee = payee;
    }

    public AccountInfo getPayer() {
        return payer;
    }

    public void setPayer(AccountInfo payer) {
        this.payer = payer;
    }
}