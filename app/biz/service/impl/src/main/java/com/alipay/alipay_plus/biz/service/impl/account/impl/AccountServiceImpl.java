package com.alipay.alipay_plus.biz.service.impl.account.impl;

import com.alipay.alipay_plus.common.service.facade.api.AccountService;
import com.alipay.alipay_plus.biz.service.impl.template.AccountBizCallback;
import com.alipay.alipay_plus.common.service.facade.baseresult.AccountBizResult;
import com.alipay.alipay_plus.common.service.facade.enums.AccountResultCode;
import com.alipay.alipay_plus.common.service.facade.item.AccountInfoItem;
import com.alipay.alipay_plus.common.service.facade.item.TransactionHistoryItem;
import com.alipay.alipay_plus.common.service.facade.item.TransactionRecordItem;
import com.alipay.alipay_plus.common.service.facade.request.*;
import com.alipay.alipay_plus.core.model.domain.AccountInfo;
import com.alipay.alipay_plus.core.model.domain.TransactionHistory;
import com.alipay.alipay_plus.core.model.domain.TransactionRecord;
import com.alipay.alipay_plus.core.model.enums.AccountActionEnum;
import com.alipay.alipay_plus.core.model.enums.AccountStatusEnum;
import com.alipay.alipay_plus.core.model.util.AssertUtil;
import com.alipay.alipay_plus.biz.service.impl.checker.CheckParamUtil;
import com.alipay.alipay_plus.core.model.converter.ItemConverter;
import org.springframework.util.StringUtils;

import java.util.List;

import static io.micrometer.common.util.StringUtils.isEmpty;

public class AccountServiceImpl extends AbstractAccountBizService implements AccountService {

    @Override
    public AccountBizResult<String> createAccount(CreateAccountRequest request) {
        return accountServiceTemplate.execute(request, AccountActionEnum.CREATE_ACCOUNT,
                new AccountBizCallback<>() {
                    @Override
                    protected AccountBizResult<String> createDefaultResponse() {
                        return null;
                    }

                    @Override
                    protected void checkParams(CreateAccountRequest request) {
                        CheckParamUtil.checkCreateAccountRequest(request);
                    }

                    @Override
                    protected void process(CreateAccountRequest request, AccountBizResult<String> response) {

                    }
                });
    }

    @Override
    public AccountBizResult<String> transfer(TransferRequest request) {
        return accountServiceTemplate.execute(request, AccountActionEnum.TRANSFER,
                new AccountBizCallback<>() {
                    @Override
                    protected AccountBizResult<String> createDefaultResponse() {
                        return new AccountBizResult<>();
                    }

                    @Override
                    protected void checkParams(TransferRequest request) {
                        CheckParamUtil.checkTransferRequest(request);
                    }

                    @Override
                    protected void process(TransferRequest request, AccountBizResult<String> response) {
                        //TODO: Transfer logic.

                    }
                });
    }

    @Override
    public AccountBizResult<AccountInfoItem> queryAccountInfo(QueryAccountInfoRequest request) {
        return accountServiceTemplate.execute(request, AccountActionEnum.QUERY_ACCOUNT_INFO,
                new AccountBizCallback<>() {
                    @Override
                    protected AccountBizResult<AccountInfoItem> createDefaultResponse() {
                        return new AccountBizResult<>();
                    }

                    @Override
                    protected void checkParams(QueryAccountInfoRequest request) {
                        CheckParamUtil.checkQueryAccountInfoRequest(request);
                    }

                    @Override
                    protected void process(QueryAccountInfoRequest request, AccountBizResult<AccountInfoItem> response) {
                        // call account database
                        AccountInfo accountInfo = accountRepository.queryAccountInfo(request.getAccountId());
                        // check if account exist
                        AssertUtil.notNull(accountInfo, AccountResultCode.ACCOUNT_NOT_FOUND, "Account not found");
                        // check if account status is valid
                        AssertUtil.isTrue(accountInfo.getStatus().equals(AccountStatusEnum.ACTIVE.getCode()),
                                AccountResultCode.ACCOUNT_STATUS_ILLEGAL, "Account status is not valid");

                        AssertUtil.isTrue(request.getOperatorId().equals(accountInfo.getAccRelationId()), AccountResultCode.INVALID_REQUEST,
                                "User not permitted to access account data");

                        // convert DO to DTO and set result.
                        response.setResult(ItemConverter.convertToItem(accountInfo));
                    }
                });
    }

    @Override
    public AccountBizResult<TransactionRecordItem> queryTransactionRecord(QueryTransactionRecordRequest request) {
        return accountServiceTemplate.execute(request, AccountActionEnum.QUERY_TRANSACTION_RECORD,
                new AccountBizCallback<>() {
                    @Override
                    protected AccountBizResult<TransactionRecordItem> createDefaultResponse() {
                        return new AccountBizResult<>();
                    }

                    @Override
                    protected void checkParams(QueryTransactionRecordRequest request) {
                        CheckParamUtil.checkQueryTransactionRecordRequest(request);
                    }

                    @Override
                    protected void process(QueryTransactionRecordRequest request, AccountBizResult<TransactionRecordItem> response) {
                        // call account database
                        AccountInfo accountInfo = accountRepository.queryAccountInfo(request.getAccountId());
                        // check if account exist
                        AssertUtil.notNull(accountInfo, AccountResultCode.ACCOUNT_NOT_FOUND, "Account not found");
                        // check if account status is valid
                        AssertUtil.isTrue(accountInfo.getStatus().equals(AccountStatusEnum.ACTIVE.getCode()),
                                AccountResultCode.ACCOUNT_STATUS_ILLEGAL, "Account status is not valid");

                        AssertUtil.isTrue(request.getOperatorId().equals(accountInfo.getAccRelationId()), AccountResultCode.INVALID_REQUEST,
                                "User not permitted to access account data");

                        // Query Transaction record
                        TransactionRecord transactionRecord = accountRepository.queryTransactionRecord(request);

                        // convert DO to DTO and set result.
                        response.setResult(ItemConverter.convertToItem(transactionRecord));
                    }
                });
    }

    @Override
    public AccountBizResult<List<TransactionHistoryItem>> queryTransactionHistory(QueryTransactionHistoryRequest request) {
        return accountServiceTemplate.execute(request, AccountActionEnum.QUERY_TRANSACTION_HISTORY,
                new AccountBizCallback<>() {
                    @Override
                    protected AccountBizResult<List<TransactionHistoryItem>> createDefaultResponse() {
                        return new AccountBizResult<>();
                    }

                    @Override
                    protected void checkParams(QueryTransactionHistoryRequest request) {
                        CheckParamUtil.checkQueryTransactionHistoryRequest(request);
                    }

                    @Override
                    protected void process(QueryTransactionHistoryRequest request, AccountBizResult<List<TransactionHistoryItem>> response) {
                        // call account database
                        AccountInfo accountInfo = accountRepository.queryAccountInfo(request.getAccountId());
                        // check if account exist
                        AssertUtil.notNull(accountInfo, AccountResultCode.ACCOUNT_NOT_FOUND, "Account not found");
                        // check if account status is valid
                        AssertUtil.isTrue(accountInfo.getStatus().equals(AccountStatusEnum.ACTIVE.getCode()),
                                AccountResultCode.ACCOUNT_STATUS_ILLEGAL, "Account status is not valid");

                        AssertUtil.isTrue(request.getOperatorId().equals(accountInfo.getAccRelationId()), AccountResultCode.INVALID_REQUEST,
                                "User not permitted to access account data");

                        // Query Transaction record
                        List<TransactionHistory> transactionHistory = accountRepository.queryTransactionHistory(request);

                        // convert DO to DTO and set result.
                        response.setResult(ItemConverter.convertToItem(transactionHistory));
                    }
                });
    }

    @Override
    public AccountBizResult<String> insertTransactionRecord(InsertTransactionRecordRequest request) {
        return accountServiceTemplate.execute(request, AccountActionEnum.INSERT_TRANSACTION_RECORD,
                new AccountBizCallback<>() {
                    @Override
                    protected AccountBizResult<String> createDefaultResponse() {
                        return new AccountBizResult<>();
                    }

                    @Override
                    protected void checkParams(InsertTransactionRecordRequest request) {
                        CheckParamUtil.checkInsertTransactionRecordRequest();
                    }

                    @Override
                    protected void process(InsertTransactionRecordRequest request, AccountBizResult<String> response) {
                        transactionTemplate.execute(status -> {
                            // insert sql need for update
                            TransactionRecord transactionRecord = accountRepository.insertTransactionRecord(request);
                            if (StringUtils.isEmpty(transactionRecord.getFailureReason())) {
                                return ItemConverter.convertToItem(transactionRecord);
                            }
                            return null;
                        });
                    }
                });
    }

    @Override
    public AccountBizResult<TransactionRecordItem> updateTransactionRecord(UpdateTransactionRecordRequest request) {
        return accountServiceTemplate.execute(request, AccountActionEnum.UPDATE_TRANSACTION_RECORD,
                new AccountBizCallback<>() {

                    @Override
                    protected AccountBizResult<TransactionRecordItem> createDefaultResponse() {
                        return new AccountBizResult<>();
                    }

                    @Override
                    protected void checkParams(UpdateTransactionRecordRequest request) {
                        CheckParamUtil.checkUpdateTransactionRecordRequest(request);
                    }

                    @Override
                    protected void process(UpdateTransactionRecordRequest request, AccountBizResult<TransactionRecordItem> response) {
                        // update mainly the status to PENDING from OTP_REQUESST

                    }
                });
    }


}
