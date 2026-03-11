package com.alipay.account_center.common.service.facade.api;
import com.alipay.account_center.common.service.facade.baseresult.AccountBizResult;
import com.alipay.account_center.common.service.facade.item.AccountInfoItem;
import com.alipay.account_center.common.service.facade.item.TransactionHistoryItem;
import com.alipay.account_center.common.service.facade.item.TransactionRecordItem;
import com.alipay.account_center.common.service.facade.request.*;

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
     * query transaction record
     * @param request
     * @return
     */
    @POST
    @Path("/queryTransactionRecord")
    AccountBizResult<TransactionRecordItem> queryTransactionRecord(QueryTransactionRecordRequest request);

    /**
     * query transaction history
     * @param request
     * @return
     */
    @POST
    @Path("/queryTransactionHistory")
    AccountBizResult<List<TransactionHistoryItem>> queryTransactionHistory(QueryTransactionHistoryRequest request);

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
     * publish transfer event
     * @param request
     * @return
     */
    @POST
    @Path("/publishTransfer")
    AccountBizResult<String> publishTransfer(PublishTransferRequest request);
}
