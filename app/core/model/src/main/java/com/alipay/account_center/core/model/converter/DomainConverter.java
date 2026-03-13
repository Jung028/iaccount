package com.alipay.account_center.core.model.converter;

import com.alipay.account_center.common.dal.auto.dataobject.AccountDO;
import com.alipay.account_center.common.dal.auto.dataobject.TransactionDO;
import com.alipay.account_center.common.service.facade.enums.TransactionStatusEnum;
import com.alipay.account_center.common.service.facade.enums.TransactionTypeEnum;
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
        accountInfo.setAccountRelationId(accountDO.getAccountRelationId());
        accountInfo.setCurrency(accountDO.getCurrency());
        accountInfo.setBalance(accountDO.getBalance());
        accountInfo.setStatus(accountDO.getStatus());
        accountInfo.setGmtCreate(accountDO.getGmtCreate());
        accountInfo.setGmtModified(accountDO.getGmtModified());
        accountInfo.setExtInfo(accountDO.getExtInfo());
        return accountInfo;
    }

    public static TransactionRecord convertToModel(TransactionDO transactionDO) {
    if (transactionDO == null) {
        return null;
    }
    TransactionRecord transactionRecord = new TransactionRecord();
    transactionRecord.setTxnId(transactionDO.getTxnId());
    transactionRecord.setGmtCreate(transactionDO.getGmtCreate());
    transactionRecord.setGmtModified(transactionDO.getGmtModified());
    transactionRecord.setGmtComplete(transactionDO.getGmtComplete());
    transactionRecord.setPayerAccountId(transactionDO.getPayerAccountId());
    transactionRecord.setPayeeAccountId(transactionDO.getPayeeAccountId());
    transactionRecord.setAmount(transactionDO.getAmount());
    transactionRecord.setCurrency(transactionDO.getCurrency());
    transactionRecord.setTxnType(TransactionTypeEnum.valueOf(transactionDO.getType()));
    transactionRecord.setTxnStatus(TransactionStatusEnum.valueOf(transactionDO.getStatus()));
    transactionRecord.setFailureReason(transactionDO.getFailureReason());
    transactionRecord.setExtInfo(transactionDO.getExtInfo());
    return transactionRecord;
    }

    public static List<TransactionHistory> convertToModelList(List<TransactionDO> transactionDOS) {
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
                    item.setStatus(TransactionStatusEnum.valueOf(transaction.getStatus()));
                    item.setExtInfo(transaction.getExtInfo());
                    return item;
                })
                .collect(Collectors.toList());
    }

    public static AccountDO convertToDomain(AccountInfo accountInfo) {
        if (accountInfo == null) {
            return null;
        }
        AccountDO accountDO = new AccountDO();
        accountDO.setAccountId(accountInfo.getAccountId());
        accountDO.setAccountName(accountInfo.getAccountName());
        accountDO.setAccountType(accountInfo.getAccountType());
        accountDO.setAccountRelationId(accountInfo.getAccountRelationId());
        accountDO.setCurrency(accountInfo.getCurrency());
        accountDO.setBalance(accountInfo.getBalance());
        accountDO.setStatus(accountInfo.getStatus());
        accountDO.setGmtCreate(accountInfo.getGmtCreate());
        accountDO.setGmtModified(accountInfo.getGmtModified());
        accountDO.setExtInfo(accountInfo.getExtInfo());
        return accountDO;
    }
}
