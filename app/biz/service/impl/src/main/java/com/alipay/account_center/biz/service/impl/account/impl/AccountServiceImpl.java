package com.alipay.account_center.biz.service.impl.account.impl;

import com.alipay.account_center.biz.service.impl.helper.ResponseBuilder;
import com.alipay.account_center.common.service.facade.enums.TransactionStatusEnum;
import com.alipay.account_center.common.service.facade.event.EcTransactionEvent;
import com.alipay.account_center.common.service.facade.request.*;
import com.alipay.sofa.runtime.api.annotation.SofaService;
import com.alipay.sofa.runtime.api.annotation.SofaServiceBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import com.alipay.account_center.common.service.facade.api.AccountService;
import com.alipay.account_center.biz.service.impl.template.AccountBizCallback;
import com.alipay.account_center.common.service.facade.baseresult.AccountBizResult;
import com.alipay.account_center.common.service.facade.enums.AccountResultCode;
import com.alipay.account_center.common.service.facade.item.AccountInfoItem;
import com.alipay.account_center.common.service.facade.item.TransactionRecordItem;
import com.alipay.account_center.core.model.domain.AccountInfo;
import com.alipay.account_center.core.model.domain.TransactionHistory;
import com.alipay.account_center.core.model.domain.TransactionRecord;
import com.alipay.account_center.core.model.enums.AccountActionEnum;
import com.alipay.account_center.core.model.enums.AccountStatusEnum;
import com.alipay.account_center.core.model.util.AssertUtil;
import com.alipay.account_center.biz.service.impl.checker.CheckParamUtil;
import com.alipay.account_center.core.model.converter.ItemConverter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@SofaService(
        interfaceType = AccountService.class,
        uniqueId = "",
        bindings = {
                @SofaServiceBinding(bindingType = "bolt"),
                @SofaServiceBinding(bindingType = "rest")
        }
)
public class AccountServiceImpl extends AbstractAccountBizService implements AccountService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public AccountBizResult<String> createAccount(CreateAccountRequest request) {
        return accountServiceTemplate.execute(request, AccountActionEnum.CREATE_ACCOUNT,
                new AccountBizCallback<>() {
                    @Override
                    protected AccountBizResult<String> createDefaultResponse() {
                        return new AccountBizResult<>();
                    }

                    @Override
                    protected void checkParams(CreateAccountRequest request) {
                        CheckParamUtil.checkCreateAccountRequest(request);
                    }

                    @Override
                    protected void process(CreateAccountRequest request, AccountBizResult<String> result) {
                        AccountInfo accountInfo = accountRepository.createAccount(request);
                        if (accountInfo != null) {
                            ResponseBuilder.success(result, accountInfo.getAccountId(), AccountActionEnum.CREATE_ACCOUNT.getCode(),
                                    AccountActionEnum.CREATE_ACCOUNT.getDesc());
                        } else {
                            ResponseBuilder.fail(result, AccountActionEnum.CREATE_ACCOUNT.getCode(),
                                    AccountActionEnum.CREATE_ACCOUNT.getDesc());
                        }
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

                        response.setSuccess(true);
                        response.setResult(ItemConverter.convertToItem(accountInfo));
                    }
                });
    }

    @Override
    public AccountBizResult<AccountInfoItem> queryAccountInfoByUserId(QueryAccountInfoRequest request) {
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
                        AccountInfo accountInfo = accountRepository.queryAccountInfoByUserId(request.getUserId());
                        // check if account exist
                        AssertUtil.notNull(accountInfo, AccountResultCode.ACCOUNT_NOT_FOUND, "Account not found");
                        // check if account status is valid
                        AssertUtil.isTrue(accountInfo.getStatus().equals(AccountStatusEnum.ACTIVE.getCode()),
                                AccountResultCode.ACCOUNT_STATUS_ILLEGAL, "Account status is not valid");

                        response.setSuccess(true);
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

                        // Query Transaction record
                        TransactionRecord transactionRecord = accountTransactionRepository.queryTransactionRecord(request);

                        response.setSuccess(true);
                        response.setResult(ItemConverter.convertToItem(transactionRecord));
                    }
                });
    }

    @Override
    public AccountBizResult<TransactionRecordItem> queryTransactionByStatus(QueryTransactionRecordRequest request) {
        return accountServiceTemplate.execute(request, AccountActionEnum.QUERY_TRANSACTION_BY_STATUS,
                new AccountBizCallback<>() {
                    @Override
                    protected AccountBizResult<TransactionRecordItem> createDefaultResponse() {
                        return new AccountBizResult<>();
                    }

                    @Override
                    protected void checkParams(QueryTransactionRecordRequest request) {
                        CheckParamUtil.checkQueryTransactionByStatus(request);
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

                        // Query Transaction record
                        TransactionRecord transactionRecord = accountTransactionRepository.queryTransactionByStatus(request);

                        response.setSuccess(true);
                        response.setResult(ItemConverter.convertToItem(transactionRecord));
                    }
                });
    }

    @Override
    public AccountBizResult<QueryTransactionHistoryResult> queryTransactionHistory(QueryTransactionHistoryRequest request) {
        return accountServiceTemplate.execute(request, AccountActionEnum.QUERY_TRANSACTION_HISTORY,
                new AccountBizCallback<>() {
                    @Override
                    protected AccountBizResult<QueryTransactionHistoryResult> createDefaultResponse() {
                        return new AccountBizResult<>();
                    }

                    @Override
                    protected void checkParams(QueryTransactionHistoryRequest request) {
                        CheckParamUtil.checkQueryTransactionHistoryRequest(request);
                    }

                    @Override
                    protected void process(QueryTransactionHistoryRequest request, AccountBizResult<QueryTransactionHistoryResult> response) {
                        // call account database
                        AccountInfo accountInfo = accountRepository.queryAccountInfo(request.getAccountId());
                        // check if account exist
                        AssertUtil.notNull(accountInfo, AccountResultCode.ACCOUNT_NOT_FOUND, "Account not found");
                        // check if account status is valid
                        AssertUtil.isTrue(accountInfo.getStatus().equals(AccountStatusEnum.ACTIVE.getCode()),
                                AccountResultCode.ACCOUNT_STATUS_ILLEGAL, "Account status is not valid");

                        // Query Transaction record
                        List<TransactionHistory> transactionHistory = accountTransactionRepository.queryTransactionHistory(request);

                        // Query total count
                        int totalCount = accountTransactionRepository.queryTransactionTotalCount(request);

                        QueryTransactionHistoryResult queryTransactionHistoryResult = new QueryTransactionHistoryResult();
                        queryTransactionHistoryResult.setTotalCount(totalCount);
                        queryTransactionHistoryResult.setTransactionHistoryList(ItemConverter.convertToItem(transactionHistory));

                        // convert DO to DTO and set result.
                        response.setSuccess(true);
                        response.setResult(queryTransactionHistoryResult);
                    }
                });
    }

    @Override
    public AccountBizResult<TransactionRecordItem> insertTransactionRecord(InsertTransactionRecordRequest request) {
        return accountServiceTemplate.execute(request, AccountActionEnum.INSERT_TRANSACTION_RECORD,
                new AccountBizCallback<>() {
                    @Override
                    protected AccountBizResult<TransactionRecordItem> createDefaultResponse() {
                        return new AccountBizResult<>();
                    }

                    @Override
                    protected void checkParams(InsertTransactionRecordRequest request) {
                        CheckParamUtil.checkInsertTransactionRecordRequest();
                    }

                    @Override
                    protected void process(InsertTransactionRecordRequest request, AccountBizResult<TransactionRecordItem> response) {
                        TransactionRecord transactionRecord =
                                transactionTemplate.execute(status ->
                                        accountTransactionRepository.insertTransactionRecord(request)
                                );
                        if (transactionRecord != null) {
                            response.setSuccess(true);
                            response.setResult(ItemConverter.convertToItem(transactionRecord));
                        }
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
                    protected void process(UpdateTransactionRecordRequest request,
                                           AccountBizResult<TransactionRecordItem> response) {
                        TransactionRecord transactionRecord =
                                transactionTemplate.execute(status -> {
                                    // Step 1: update (returns int)
                                    accountTransactionRepository.updateTransactionRecord(request);

                                    // Step 2: query the updated record
                                    QueryTransactionRecordRequest queryTransactionRecordRequest = new QueryTransactionRecordRequest();
                                    queryTransactionRecordRequest.setTxnId(request.getTxnId());
                                    queryTransactionRecordRequest.setAccountId(request.getAccountId());
                                    return accountTransactionRepository.queryTransactionRecord(queryTransactionRecordRequest);
                                });
                        if (transactionRecord != null) {
                            response.setSuccess(true);
                            response.setResult(ItemConverter.convertToItem(transactionRecord));
                        }
                    }
                });
    }

}
