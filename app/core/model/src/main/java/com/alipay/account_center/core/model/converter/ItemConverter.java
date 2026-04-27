package com.alipay.account_center.core.model.converter;

import com.alipay.account_center.common.service.facade.enums.TransactionDirection;
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
        accountInfoItem.setAccountRelationId(accountInfo.getAccountRelationId());
        accountInfoItem.setCurrency(accountInfo.getCurrency());
        accountInfoItem.setBalance(accountInfo.getBalance());
        accountInfoItem.setStatus(accountInfo.getStatus());
        accountInfoItem.setGmtCreate(accountInfo.getGmtCreate());
        accountInfoItem.setGmtModified(accountInfo.getGmtModified());
        accountInfoItem.setExtInfo(accountInfo.getExtInfo());
        return accountInfoItem;
    }

    public static TransactionRecordItem convertToItem(TransactionRecord transactionRecord, String accountId) {
        if(transactionRecord == null) {
            return new TransactionRecordItem();
        }
        TransactionRecordItem transactionRecordItem = new TransactionRecordItem();
        // add direction, for perspective
        if (transactionRecord.getPayeeAccountId().equals(accountId)) {
            transactionRecordItem.setTxnDirection(TransactionDirection.CREDIT);
        } else {
            transactionRecordItem.setTxnDirection(TransactionDirection.DEBIT);
        }
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
        transactionRecordItem.setExtInfo(transactionRecord.getExtInfo());
        return transactionRecordItem;
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
        transactionRecordItem.setExtInfo(transactionRecord.getExtInfo());
        return transactionRecordItem;
    }

    public static List<TransactionHistoryItem> convertToItem(List<TransactionHistory> transactionHistory,
                                                             String accountId) {
        if (transactionHistory == null || transactionHistory.isEmpty()) {
            return Collections.emptyList();
        }
        return transactionHistory.stream().map(transaction -> {
            TransactionHistoryItem item = new TransactionHistoryItem();
            // add direction, for perspective
            if (transaction.getPayeeAccountId().equals(accountId)) {
                item.setTransactionDirection(TransactionDirection.CREDIT);
            } else {
                item.setTransactionDirection(TransactionDirection.DEBIT);
            }
            item.setTxnId(transaction.getTxnId());
            item.setGmtCreate(transaction.getGmtCreate());
            item.setPayeeAccountId(transaction.getPayeeAccountId());
            item.setCompletedAt(transaction.getCompletedAt());
            item.setTransactionType(transaction.getTransactionType());
            item.setAmount(transaction.getAmount());
            item.setCurrency(transaction.getCurrency());
            item.setStatus(transaction.getStatus());
            item.setExtInfo(transaction.getExtInfo());
            return item;
        }).collect(Collectors.toList());
    }
}
