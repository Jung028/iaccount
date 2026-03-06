package com.alipay.alipay_plus.core.model.converter;


import com.alipay.account.common.dal.auto.dataobject.AccountTransactionDO;
import com.alipay.alipay_plus.common.service.facade.enums.TransactionStatusEnum;
import com.alipay.alipay_plus.common.service.facade.enums.TransactionTypeEnum;
import com.alipay.alipay_plus.core.model.domain.TransactionRecord;

/**
 * @author jung
 * @date 2026-02-14 15:43:36
 */
public class ModelConverter {

    private TransactionRecord convertToModel(AccountTransactionDO accountTransactionDO) {
        if (accountTransactionDO == null) {
            return null;
        }
        TransactionRecord transactionRecord = new TransactionRecord();
        transactionRecord.setTxnId(accountTransactionDO.getTxnId());
        transactionRecord.setGmtCreate(accountTransactionDO.getGmtCreate());
        transactionRecord.setGmtModified(accountTransactionDO.getGmtModified());
        transactionRecord.setGmtComplete(accountTransactionDO.getGmtComplete());
        transactionRecord.setPayerAccountId(accountTransactionDO.getPayerAccountId());
        transactionRecord.setPayeeAccountId(accountTransactionDO.getPayeeAccountId());
        transactionRecord.setAmount(accountTransactionDO.getAmount());
        transactionRecord.setCurrency(accountTransactionDO.getCurrency());
        transactionRecord.setTxnType(TransactionTypeEnum.valueOf(accountTransactionDO.getTxntype()));
        transactionRecord.setTxnStatus(TransactionStatusEnum.valueOf(accountTransactionDO.getStatus()));
        transactionRecord.setFailureReason(accountTransactionDO.getFailureReason());
        transactionRecord.setDesc(accountTransactionDO.getDesc());
        return transactionRecord;
    }

}