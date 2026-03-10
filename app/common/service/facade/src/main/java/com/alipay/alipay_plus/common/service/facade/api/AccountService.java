package com.alipay.alipay_plus.common.service.facade.api;

import com.alipay.alipay_plus.common.service.facade.baseresult.AccountBizResult;
import com.alipay.alipay_plus.common.service.facade.item.AccountInfoItem;
import com.alipay.alipay_plus.common.service.facade.item.TransactionHistoryItem;
import com.alipay.alipay_plus.common.service.facade.item.TransactionRecordItem;
import com.alipay.alipay_plus.common.service.facade.request.*;

import java.util.List;

public interface AccountService {

    /**
     * Create account
     * Called by UserService
     * @param request
     * @return
     */
    AccountBizResult<String> createAccount(CreateAccountRequest request);

    /**
     * query account info
     * @param request
     * @return
     */
    AccountBizResult<AccountInfoItem> queryAccountInfo(QueryAccountInfoRequest request);

    /**
     * query transaction record
     * @param request
     * @return
     */
    AccountBizResult<TransactionRecordItem> queryTransactionRecord(QueryTransactionRecordRequest request);

    /**
     * query transaction history
     * @param request
     * @return
     */
    AccountBizResult<List<TransactionHistoryItem>> queryTransactionHistory(QueryTransactionHistoryRequest request);

    /**
     * insert new transaction record
     * @param request
     * @return
     */
    AccountBizResult<TransactionRecordItem> insertTransactionRecord(InsertTransactionRecordRequest request);

    /**
     * update transaction record
     * @param request
     * @return
     */
    AccountBizResult<TransactionRecordItem> updateTransactionRecord(UpdateTransactionRecordRequest request);

    /**
     * publish transfer event
     * @param request
     * @return
     */
    AccountBizResult<String> publishTransfer(PublishTransferRequest request);
}
