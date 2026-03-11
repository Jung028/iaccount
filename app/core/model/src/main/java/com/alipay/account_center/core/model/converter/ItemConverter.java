package com.alipay.account_center.core.model.converter;

import com.alipay.account_center.common.service.facade.item.AccountInfoItem;
import com.alipay.account_center.common.service.facade.item.TransactionHistoryItem;
import com.alipay.account_center.common.service.facade.item.TransactionRecordItem;
import com.alipay.account_center.core.model.domain.AccountInfo;
import com.alipay.account_center.core.model.domain.TransactionHistory;
import com.alipay.account_center.core.model.domain.TransactionRecord;

import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;

public class ItemConverter {

    public static AccountInfoItem convertToItem(AccountInfo accountInfo) {
        if (accountInfo == null) {
            return new AccountInfoItem();
        }
        AccountInfoItem accountInfoItem = new AccountInfoItem();
        accountInfoItem.setAccountId(accountInfo.getAccountId());
        accountInfoItem.setAccountNumber(accountInfo.getAccountNumber());
        accountInfoItem.setAccountName(accountInfo.getAccountName());
        accountInfoItem.setAccountType(accountInfo.getAccountType());
        accountInfoItem.setAccRelationId(accountInfo.getAccRelationId());
        accountInfoItem.setCurrency(accountInfo.getCurrency());
        accountInfoItem.setBalance(accountInfo.getBalance());
        accountInfoItem.setStatus(accountInfo.getStatus());
        accountInfoItem.setGmtCreate(accountInfo.getGmtCreate());
        accountInfoItem.setGmtModified(accountInfo.getGmtModified());
        accountInfoItem.setExtInfo(accountInfo.getExtInfo());
        return accountInfoItem;
    }

    public static TransactionRecordItem convertToItem(TransactionRecord transactionRecord) {
        if(transactionRecord == null) {
            return new TransactionRecordItem();
        }
        TransactionRecordItem transactionRecordItem = new TransactionRecordItem();
        transactionRecordItem.setTxnId(transactionRecord.getTxnId());
        transactionRecordItem.setGmtCreate(transactionRecord.getGmtCreate());
        transactionRecordItem.setGmtModified(transactionRecord.getGmtModified());
        transactionRecordItem.setGmtComplete(transactionRecord.getGmtComplete());
        transactionRecordItem.setPayerAccountId(transactionRecord.getPayerAccountId());
        transactionRecordItem.setPayeeAccountId(transactionRecord.getPayeeAccountId());
        transactionRecordItem.setAmount(transactionRecord.getAmount());
        transactionRecordItem.setCurrency(transactionRecord.getCurrency());
        transactionRecordItem.setTxnType(transactionRecord.getTxnType());
        transactionRecordItem.setTxnStatus(transactionRecord.getTxnStatus());
        transactionRecordItem.setFailureReason(transactionRecord.getFailureReason());
        transactionRecordItem.setDesc(transactionRecord.getDesc());
        return transactionRecordItem;
    }

    public static List<TransactionHistoryItem> convertToItem(List<TransactionHistory> transactionHistory) {
        if (transactionHistory == null || transactionHistory.isEmpty()) {
            return Collections.emptyList();
        }
        return transactionHistory.stream().map(transaction -> {
            TransactionHistoryItem item = new TransactionHistoryItem();
            item.setTxnId(transaction.getTxnId());
            item.setGmtCreate(transaction.getGmtCreate());
            item.setAmount(transaction.getAmount());
            item.setCurrency(transaction.getCurrency());
            item.setDirection(transaction.getDirection());
            item.setStatus(transaction.getStatus());
            item.setDesc(transaction.getDesc());
            return item;
        }).collect(Collectors.toList());
    }
}
