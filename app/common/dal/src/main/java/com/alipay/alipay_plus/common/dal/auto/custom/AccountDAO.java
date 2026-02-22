package com.alipay.alipay_plus.common.dal.auto.custom;

import com.alipay.alipay_plus.common.dal.auto.dataobject.AccountDO;
import com.alipay.alipay_plus.common.dal.auto.dataobject.AccountTransactionDO;
import com.alipay.alipay_plus.common.service.facade.enums.TransactionStatusEnum;

import javax.money.MonetaryAmount;
import java.util.List;

public interface AccountDAO {
    AccountDO queryAccountInfo(String accountId);

    AccountTransactionDO queryTransactionRecord(String transactionId);

    List<AccountTransactionDO> queryTransactionHistory(String txnId, String pageSize, String offSet);

    AccountTransactionDO insertTransactionRecord(AccountTransactionDO accountTransactionDO);

    AccountTransactionDO updateTransactionRecord(AccountTransactionDO record);
}
