package com.alipay.account_center.core.model.converter;

import com.alipay.account_center.common.dal.auto.dataobject.AccountDO;
import com.alipay.account_center.common.dal.auto.dataobject.AccountTransactionDO;
import com.alipay.account_center.common.service.facade.enums.TransactionDirectionEnum;
import com.alipay.account_center.common.service.facade.enums.TransactionStatusEnum;
import com.alipay.account_center.core.model.domain.AccountInfo;
import com.alipay.account_center.core.model.domain.TransactionHistory;
import com.alipay.account_center.core.model.domain.TransactionRecord;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class DomainConverter {

    /**
     * convert account info DO to model
     * @param accountDO
     * @return
     */
    public static AccountInfo convertToModel(AccountDO accountDO) {
        if (accountDO == null) {
            return null;
        }
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setAccountId(accountDO.getAccountId());
        accountInfo.setAccountName(accountDO.getAccountName());
        accountInfo.setAccountType(accountDO.getAccountType());
        accountInfo.setAccRelationId(accountDO.getAccountRelationId());
        accountInfo.setCurrency(accountDO.getCurrency());
        accountInfo.setBalance(accountDO.getBalance());
        accountInfo.setStatus(accountDO.getStatus());
        accountInfo.setGmtCreate(accountDO.getGmtCreate());
        accountInfo.setGmtModified(accountDO.getGmtModified());
        accountInfo.setExtInfo(accountDO.getExtInfo());
        return accountInfo;
    }

    public static TransactionRecord convertToModel(AccountTransactionDO accountTransactionDO) {
    if (accountTransactionDO == null) {
        return null;
    }
    TransactionRecord transactionRecord = new TransactionRecord();
    transactionRecord.setTxnId(transactionRecord.getTxnId());
    transactionRecord.setGmtCreate(transactionRecord.getGmtCreate());
    transactionRecord.setGmtModified(transactionRecord.getGmtModified());
    transactionRecord.setGmtComplete(transactionRecord.getGmtComplete());
    transactionRecord.setPayerAccountId(transactionRecord.getPayerAccountId());
    transactionRecord.setPayeeAccountId(transactionRecord.getPayeeAccountId());
    transactionRecord.setAmount(transactionRecord.getAmount());
    transactionRecord.setCurrency(transactionRecord.getCurrency());
    transactionRecord.setTxnType(transactionRecord.getTxnType());
    transactionRecord.setTxnStatus(transactionRecord.getTxnStatus());
    transactionRecord.setFailureReason(transactionRecord.getFailureReason());
    transactionRecord.setDesc(transactionRecord.getDesc());
    return transactionRecord;
    }

    public static List<TransactionHistory> convertToModelList(List<AccountTransactionDO> transactionDOS) {
        if (transactionDOS == null || transactionDOS.isEmpty()) {
            return Collections.emptyList();
        }

        return transactionDOS.stream()
                .filter(Objects::nonNull)
                .map(transaction -> {
                    TransactionHistory item = new TransactionHistory();
                    item.setTxnId(transaction.getTxnId());
                    item.setGmtCreate(transaction.getGmtCreate());
                    item.setAmount(transaction.getAmount());
                    item.setCurrency(transaction.getCurrency());
                    item.setDirection(TransactionDirectionEnum.valueOf(transaction.getDirection()));
                    item.setStatus(TransactionStatusEnum.valueOf(transaction.getStatus()));
                    item.setDesc(transaction.getDesc());
                    return item;
                })
                .collect(Collectors.toList());
    }
}
