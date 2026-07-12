package com.alipay.account_center.biz.service.impl.account.impl;

import com.alipay.account_center.biz.service.impl.helper.ResponseBuilder;
import com.alipay.account_center.common.service.facade.enums.TxnQueryType;
import com.alipay.account_center.common.dal.auto.dataobject.TransactionMetricsDTO;
import com.alipay.account_center.common.service.facade.item.MetricCard;
import com.alipay.account_center.common.service.facade.item.TransactionHistoryItem;
import com.alipay.account_center.common.service.facade.request.*;
import com.alipay.account_center.common.service.facade.result.QueryAverageBasketResult;
import com.alipay.account_center.common.service.facade.result.QueryNewCustomerCountResult;
import com.alipay.account_center.common.service.facade.result.QueryTotalRevenueResult;
import com.alipay.sofa.runtime.api.annotation.SofaService;
import com.alipay.sofa.runtime.api.annotation.SofaServiceBinding;
import org.apache.zookeeper.Op;
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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
                        //TODO: Add compatability for merchant to create account as well. change userId to ownerId.
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
                        response.setResult(ItemConverter.convertToItem(transactionRecord, request.getAccountId()));
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
                        AccountInfo accountInfo = accountRepository.queryAccountInfo(request.getAccountId());
                        // check if account exist
                        AssertUtil.notNull(accountInfo, AccountResultCode.ACCOUNT_NOT_FOUND, "Account not found");
                        // check if account status is valid
                        AssertUtil.isTrue(accountInfo.getStatus().equals(AccountStatusEnum.ACTIVE.getCode()),
                                AccountResultCode.ACCOUNT_STATUS_ILLEGAL, "Account status is not valid");

                        List<TransactionHistory> transactionHistory = accountTransactionRepository.queryTransactionHistory(request);

                        // Query total count
                        int totalCount = accountTransactionRepository.queryTransactionTotalCount(request);

                        QueryTransactionHistoryResult queryTransactionHistoryResult = new QueryTransactionHistoryResult();
                        queryTransactionHistoryResult.setTotalCount(totalCount);
                        queryTransactionHistoryResult.setTransactionHistoryList(ItemConverter.convertToItem(transactionHistory, request.getAccountId()));

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
                            response.setResult(ItemConverter.convertToItem(transactionRecord, request.getPayerAccountNo()));
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

    @Override
    public AccountBizResult<QueryTotalRevenueResult> queryTotalRevenue(QueryTotalRevenueRequest request) {
        return accountServiceTemplate.execute(request, AccountActionEnum.QUERY_MERCHANT_TRANSACTION_HISTORY,
                new AccountBizCallback<>() {
                    @Override
                    protected AccountBizResult<QueryTotalRevenueResult> createDefaultResponse() {
                        return new AccountBizResult<>();
                    }

                    @Override
                    protected void checkParams(QueryTotalRevenueRequest request) {
                        CheckParamUtil.checkQueryTotalRevenueRequest(request);
                    }

                    @Override
                    protected void process(QueryTotalRevenueRequest request, AccountBizResult<QueryTotalRevenueResult> response) {
                        LocalDateTime thisMonday = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
                        LocalDateTime thisSunday = thisMonday.plusDays(6).withHour(23).withMinute(59).withSecond(59);

                        int thisWeek = merchantTransactionRepository.queryTotalRevenue(request.getMerchantId(), thisMonday, thisSunday);
                        int lastWeek = merchantTransactionRepository.queryTotalRevenue(request.getMerchantId(), thisMonday.minusWeeks(1), thisSunday.minusWeeks(1));

                        Double percentage = null;
                        if (lastWeek == 0 && thisWeek == 0) {
                            percentage = 0.0;
                        } else if (lastWeek != 0) {
                            percentage = Math.round(((double)(thisWeek - lastWeek) / lastWeek) * 10000.0) / 100.0;
                        }

                        MetricCard<BigDecimal> card = new MetricCard<>();
                        card.setData(BigDecimal.valueOf(thisWeek));
                        card.setPercentageChange(percentage);
                        QueryTotalRevenueResult result = new QueryTotalRevenueResult();
                        result.setTotalRevenue(card);

                        ResponseBuilder.success(response, result, AccountResultCode.QUERY_TOTAL_REVENUE.getCode(),
                                AccountResultCode.QUERY_TOTAL_REVENUE.getDescription());
                    }
                });
    }

    @Override
    public AccountBizResult<QueryTransactionHistoryMerchantDashboard> queryTransactionHistoryForMerchantDashboard(QueryTransactionHistoryRequest request) {
        return accountServiceTemplate.execute(request, AccountActionEnum.QUERY_MERCHANT_TRANSACTION_HISTORY,
                new AccountBizCallback<>() {
                    @Override
                    protected AccountBizResult<QueryTransactionHistoryMerchantDashboard> createDefaultResponse() {
                        return new AccountBizResult<>();
                    }

                    @Override
                    protected void checkParams(QueryTransactionHistoryRequest request) {
                        CheckParamUtil.checkQueryTransactionHistoryRequest(request);
                    }

                    @Override
                    protected void process(QueryTransactionHistoryRequest request, AccountBizResult<QueryTransactionHistoryMerchantDashboard> response) {
                        AccountInfo accountInfo = accountRepository.queryAccountInfo(request.getAccountId());
                        // check if account exist
                        AssertUtil.notNull(accountInfo, AccountResultCode.ACCOUNT_NOT_FOUND, "Account not found");
                        // check if account status is valid
                        AssertUtil.isTrue(accountInfo.getStatus().equals(AccountStatusEnum.ACTIVE.getCode()),
                                AccountResultCode.ACCOUNT_STATUS_ILLEGAL, "Account status is not valid");

                        // query merchant transaction history, by the set range of this and last week.
                        LocalDateTime thisMonday = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
                        LocalDateTime thisSunday = thisMonday.plusDays(6).withHour(23).withMinute(59).withSecond(59);

                        // if query type is merchant, query by merchant txn repository
                        List<TransactionHistory> transactionHistory = merchantTransactionRepository.
                                    queryCustomerTransactionHistoryByMerchantId(request, thisMonday, thisSunday);

                        // Query total count
                        int countTransactionHistoryThisWeek = transactionHistory.size();
                        List<TransactionHistory> transactionHistoryLastWeek = merchantTransactionRepository.
                                queryCustomerTransactionHistoryByMerchantId(request, thisMonday.minusWeeks(1), thisSunday.minusWeeks(1));

                        int countTransactionHistoryLastWeek = transactionHistoryLastWeek.size();

                        Double percentage = null;
                        if (countTransactionHistoryThisWeek == 0 && countTransactionHistoryLastWeek == 0) {
                            percentage = 0.0;
                        } else if (countTransactionHistoryLastWeek != 0) {
                            percentage = Math.round(((double)(countTransactionHistoryThisWeek - countTransactionHistoryLastWeek) / countTransactionHistoryLastWeek) * 10000.0) / 100.0;
                        }

                        MetricCard<Integer> card = new MetricCard<>();
                        card.setData(countTransactionHistoryLastWeek);
                        card.setPercentageChange(percentage);
                        QueryTransactionHistoryMerchantDashboard result = new QueryTransactionHistoryMerchantDashboard();
                        result.setTransactions(card);

                        ResponseBuilder.success(response, result, AccountActionEnum.QUERY_MERCHANT_TRANSACTION_HISTORY.getCode(),
                                AccountActionEnum.QUERY_MERCHANT_TRANSACTION_HISTORY.getDesc());

                    }
                });
    }

    @Override
    public AccountBizResult<QueryAverageBasketResult> queryAverageBasket(QueryAverageBasketRequest request) {
        return accountServiceTemplate.execute(request, AccountActionEnum.QUERY_AVERAGE_BASKET,
                new AccountBizCallback<>() {
                    @Override
                    protected AccountBizResult<QueryAverageBasketResult> createDefaultResponse() {
                        return new AccountBizResult<>();
                    }

                    @Override
                    protected void checkParams(QueryAverageBasketRequest request) {
                        CheckParamUtil.checkQueryAverageBasketRequest(request);
                    }

                    @Override
                    protected void process(QueryAverageBasketRequest request, AccountBizResult<QueryAverageBasketResult> response) {
                        LocalDateTime thisMonday = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
                        LocalDateTime thisSunday = thisMonday.plusDays(6).withHour(23).withMinute(59).withSecond(59);

                        double thisWeek = merchantTransactionRepository.queryAverageBasket(request.getMerchantId(), thisMonday, thisSunday);
                        double lastWeek = merchantTransactionRepository.queryAverageBasket(request.getMerchantId(), thisMonday.minusWeeks(1), thisSunday.minusWeeks(1));

                        double percentage = 0;
                        if (lastWeek != 0) {
                            percentage = Math.round(((thisWeek - lastWeek) / lastWeek) * 10000.0) / 100.0;
                        }

                        MetricCard<Double> card = new MetricCard<>();
                        card.setData(thisWeek);
                        card.setPercentageChange(percentage);

                        QueryAverageBasketResult result = new QueryAverageBasketResult();
                        result.setAverageBasket(card);

                        ResponseBuilder.success(response, result, AccountResultCode.QUERY_AVERAGE_BASKET.getCode(),
                                AccountResultCode.QUERY_AVERAGE_BASKET.getDescription());
                    }
                });
    }

    @Override
    public AccountBizResult<QueryNewCustomerCountResult> queryNewCustomers(QueryNewCustomerCountRequest request) {
        return accountServiceTemplate.execute(request, AccountActionEnum.QUERY_NEW_CUSTOMER_COUNT,
                new AccountBizCallback<>() {
                    @Override
                    protected AccountBizResult<QueryNewCustomerCountResult> createDefaultResponse() {
                        return new AccountBizResult<>();
                    }

                    @Override
                    protected void checkParams(QueryNewCustomerCountRequest request) {
                        CheckParamUtil.checkQueryNewCustomerCountRequest(request);
                    }

                    @Override
                    protected void process(QueryNewCustomerCountRequest request, AccountBizResult<QueryNewCustomerCountResult> response) {
                        LocalDateTime thisMonday = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
                        LocalDateTime thisSunday = thisMonday.plusDays(6).withHour(23).withMinute(59).withSecond(59);

                        int thisWeek = merchantTransactionRepository.queryNewCustomerCount(request.getMerchantId(), thisMonday, thisSunday);
                        int lastWeek = merchantTransactionRepository.queryNewCustomerCount(request.getMerchantId(), thisMonday.minusWeeks(1), thisSunday.minusWeeks(1));

                        double percentage = 0;
                        if (lastWeek != 0) {
                            percentage = Math.round(((double)(thisWeek - lastWeek) / lastWeek) * 10000.0) / 100.0;
                        }

                        MetricCard<Integer> card = new MetricCard<>();
                        card.setData(thisWeek);
                        card.setPercentageChange(percentage);

                        QueryNewCustomerCountResult result = new QueryNewCustomerCountResult();
                        result.setCustomerCount(card);

                        ResponseBuilder.success(response, result, AccountResultCode.QUERY_NEW_CUSTOMER_COUNT.getCode(),
                                AccountResultCode.QUERY_NEW_CUSTOMER_COUNT.getDescription());
                    }
                });
    }
}
