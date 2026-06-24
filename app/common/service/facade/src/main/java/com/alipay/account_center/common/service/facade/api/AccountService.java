package com.alipay.account_center.common.service.facade.api;
import com.alipay.account_center.common.service.facade.baseresult.AccountBizResult;
import com.alipay.account_center.common.service.facade.item.AccountInfoItem;
import com.alipay.account_center.common.service.facade.item.TransactionHistoryItem;
import com.alipay.account_center.common.service.facade.item.TransactionRecordItem;
import com.alipay.account_center.common.service.facade.request.*;
import com.alipay.account_center.common.service.facade.result.QueryAverageBasketResult;
import com.alipay.account_center.common.service.facade.result.QueryNewCustomerCountResult;
import com.alipay.account_center.common.service.facade.result.QueryTotalRevenueResult;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/accountService")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AccountService {

    /**
     * Create account
...
     * Called by UserService
     * @param request
     * @return
     */
    @POST
    @Path("/createAccount")
    AccountBizResult<String> createAccount(CreateAccountRequest request);

    /**
     * query account info
     * @param request
     * @return
     */
    @POST
    @Path("/queryAccountInfo")
    AccountBizResult<AccountInfoItem> queryAccountInfo(QueryAccountInfoRequest request);

    /**
     * query account by user id
     * @param request
     * @return
     */
    @POST
    @Path("/queryAccountInfoByUserId")
    AccountBizResult<AccountInfoItem> queryAccountInfoByUserId(QueryAccountInfoRequest request);


    /**
     * query transaction record
     * @param request
     * @return
     */
    @POST
    @Path("/queryTransactionRecord")
    AccountBizResult<TransactionRecordItem> queryTransactionRecord(QueryTransactionRecordRequest request);

    /**
     * queryTransactionByStatus
     * @param request
     * @return
     */
    @POST
    @Path("/queryTransactionByStatus")
    AccountBizResult<TransactionRecordItem> queryTransactionByStatus(QueryTransactionRecordRequest request);

    /**
     * query transaction history
     * @param request
     * @return
     */
    @POST
    @Path("/queryTransactionHistory")
    AccountBizResult<QueryTransactionHistoryResult> queryTransactionHistory(QueryTransactionHistoryRequest request);

    /**
     * insert new transaction record
     * @param request
     * @return
     */
    @POST
    @Path("/insertTransactionRecord")
    AccountBizResult<TransactionRecordItem> insertTransactionRecord(InsertTransactionRecordRequest request);

    /**
     * update transaction record
     * @param request
     * @return
     */
    @POST
    @Path("/updateTransactionRecord")
    AccountBizResult<TransactionRecordItem> updateTransactionRecord(UpdateTransactionRecordRequest request);

    /**
     * query total revenue for merchant dashboard
     * @param request
     * @return
     */
    @POST
    @Path("/queryTotalRevenue")
    AccountBizResult<QueryTotalRevenueResult> queryTotalRevenue(QueryTotalRevenueRequest request);

    /**
     * query transaction history
     */
    @POST
    @Path("/queryTransactionHistoryForMerchantDashboard")
    AccountBizResult<QueryTransactionHistoryMerchantDashboard> queryTransactionHistoryForMerchantDashboard(QueryTransactionHistoryRequest request);

    /**
     * query average basket
     * @param request
     * @return
     */
    @POST
    @Path("/queryAverageBasket")
    AccountBizResult<QueryAverageBasketResult> queryAverageBasket(QueryAverageBasketRequest request);

    /**
     * query new customers
     * @param request
     * @return
     */
    @POST
    @Path("/queryNewCustomers")
    AccountBizResult<QueryNewCustomerCountResult> queryNewCustomers(QueryNewCustomerCountRequest request);
}
