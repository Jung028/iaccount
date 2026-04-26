package com.alipay.account_center.core.model.converter;


import com.alipay.account_center.common.dal.auto.dataobject.TransactionDO;
import com.alipay.account_center.common.service.facade.enums.TransactionStatusEnum;
import com.alipay.account_center.common.service.facade.enums.TransactionType;
import com.alipay.account_center.core.model.domain.TransactionRecord;

/**
 * @author jung
 * @date 2026-02-14 15:43:36
 */
public class ModelConverter {

    private TransactionRecord convertToModel(TransactionDO TransactionDO) {
        if (TransactionDO == null) {
            return null;
        }
        TransactionRecord transactionRecord = new TransactionRecord();
        transactionRecord.setTxnId(TransactionDO.getTxnId());
        transactionRecord.setGmtCreate(TransactionDO.getGmtCreate());
        transactionRecord.setGmtModified(TransactionDO.getGmtModified());
        transactionRecord.setGmtComplete(TransactionDO.getGmtComplete());
        transactionRecord.setPayerAccountId(TransactionDO.getPayerAccountId());
        transactionRecord.setPayeeAccountId(TransactionDO.getPayeeAccountId());
        transactionRecord.setAmount(TransactionDO.getAmount());
        transactionRecord.setCurrency(TransactionDO.getCurrency());
        transactionRecord.setTxnType(TransactionType.valueOf(TransactionDO.getType()));
        transactionRecord.setTxnStatus(TransactionStatusEnum.valueOf(TransactionDO.getStatus()));
        transactionRecord.setFailureReason(TransactionDO.getFailureReason());
        transactionRecord.setExtInfo(TransactionDO.getExtInfo());
        return transactionRecord;
    }

}