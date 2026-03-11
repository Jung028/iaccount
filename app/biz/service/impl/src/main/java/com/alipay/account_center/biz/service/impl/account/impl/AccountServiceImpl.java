package com.alipay.account_center.biz.service.impl.account.impl;

import com.alipay.account_center.biz.service.impl.helper.ResponseBuilder;
import com.alipay.account_center.common.service.facade.enums.TransactionStatusEnum;
import com.alipay.account_center.common.service.facade.event.EcTransactionEvent;
import com.alipay.account_center.common.service.facade.request.*;
import com.alipay.sofa.runtime.api.annotation.SofaService;
import com.alipay.sofa.runtime.api.annotation.SofaServiceBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import com.alipay.account_center.common.service.facade.api.AccountService;
import com.alipay.account_center.biz.service.impl.template.AccountBizCallback;
import com.alipay.account_center.common.service.facade.baseresult.AccountBizResult;
import com.alipay.account_center.common.service.facade.enums.AccountResultCode;
import com.alipay.account_center.common.service.facade.item.AccountInfoItem;
import com.alipay.account_center.common.service.facade.item.TransactionHistoryItem;
import com.alipay.account_center.common.service.facade.item.TransactionRecordItem;
import com.alipay.account_center.core.model.domain.AccountInfo;
import com.alipay.account_center.core.model.domain.TransactionHistory;
import com.alipay.account_center.core.model.domain.TransactionRecord;
import com.alipay.account_center.core.model.enums.AccountActionEnum;
import com.alipay.account_center.core.model.enums.AccountStatusEnum;
import com.alipay.account_center.core.model.util.AssertUtil;
import com.alipay.account_center.biz.service.impl.checker.CheckParamUtil;
import com.alipay.account_center.core.model.converter.ItemConverter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
                        return null;
                    }

                    @Override
                    protected void checkParams(CreateAccountRequest request) {
                        CheckParamUtil.checkCreateAccountRequest(request);
                    }

                    @Override
                    protected void process(CreateAccountRequest request, AccountBizResult<String> result) {
                        validateUser(request.getOperatorId());
                        AccountInfo accountInfo = accountRepository.createAccount(request);
                        result.setSuccess(accountInfo == null);
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

                        ResponseBuilder.success(response, ItemConverter.convertToItem(accountInfo), AccountActionEnum.QUERY_ACCOUNT_INFO.getCode(),
                                AccountActionEnum.QUERY_ACCOUNT_INFO.getDesc());
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
                        TransactionRecord transactionRecord = accountTransactionRepository.queryTransactionRecord(request);

                        ResponseBuilder.success(response, ItemConverter.convertToItem(transactionRecord), AccountActionEnum.QUERY_TRANSACTION_RECORD.getCode(),
                                AccountActionEnum.QUERY_TRANSACTION_RECORD.getDesc());
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
                        List<TransactionHistory> transactionHistory = accountTransactionRepository.queryTransactionHistory(request);

                        // convert DO to DTO and set result.
                        ResponseBuilder.success(response, ItemConverter.convertToItem(transactionHistory), AccountActionEnum.QUERY_TRANSACTION_HISTORY.getCode(),
                                AccountActionEnum.QUERY_TRANSACTION_HISTORY.getDesc());
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
                        validateUser(request.getOperatorId());
                        TransactionRecord transactionRecord =
                                transactionTemplate.execute(status ->
                                        accountTransactionRepository.insertTransactionRecord(request)
                                );
                        if (transactionRecord != null && !StringUtils.isEmpty(transactionRecord.getFailureReason())) {
                            ResponseBuilder.success(response, ItemConverter.convertToItem(transactionRecord), AccountActionEnum.INSERT_TRANSACTION_RECORD.getCode(),
                                    AccountActionEnum.INSERT_TRANSACTION_RECORD.getDesc());
                        } else {
                            ResponseBuilder.fail(response, AccountResultCode.SYSTEM_EXCEPTION.getCode(), "Insert Transaction Record Fail");
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
                        validateUser(request.getOperatorId());
                        TransactionRecord transactionRecord =
                                transactionTemplate.execute(status ->
                                        accountTransactionRepository.updateTransactionRecord(request)
                                );
                        if (transactionRecord != null && !StringUtils.isEmpty(transactionRecord.getFailureReason())) {
                            ResponseBuilder.success(response, ItemConverter.convertToItem(transactionRecord), AccountActionEnum.UPDATE_TRANSACTION_RECORD.getCode(),
                                    AccountActionEnum.UPDATE_TRANSACTION_RECORD.getDesc());
                        } else {
                            ResponseBuilder.fail(response, AccountResultCode.SYSTEM_EXCEPTION.getCode(), "Update Transaction Record Fail");
                        }
                    }
                });
    }

    @Override
    public AccountBizResult<String> publishTransfer(PublishTransferRequest request) {
        return accountServiceTemplate.execute(request, AccountActionEnum.PUBLISH_TRANSFER_EVENT,
                new AccountBizCallback<>() {
                    @Override
                    protected AccountBizResult<String> createDefaultResponse() {
                        return new AccountBizResult<>();
                    }

                    @Override
                    protected void checkParams(PublishTransferRequest request) {
                        CheckParamUtil.checkPublishTransferRequest(request);
                    }

                    @Override
                    protected void process(PublishTransferRequest request, AccountBizResult<String> response) {


                        // query transaction id/ we get the txnid from the challenge id, or the otp:challenge
                        QueryTransactionRecordRequest queryTransactionRecordRequest = new QueryTransactionRecordRequest();
                        queryTransactionRecordRequest.setAccountId(request.getAccountId());
                        queryTransactionRecordRequest.setTxnId(request.getTxnId());
                        TransactionRecord transactionRecord = accountTransactionRepository.queryTransactionRecord(queryTransactionRecordRequest);
                        if (transactionRecord == null) {
                            return;
                        }
                        AssertUtil.isTrue(transactionRecord.getTxnStatus().equals(TransactionStatusEnum.OTP_OVER_LIMIT)
                                        || transactionRecord.getTxnStatus().equals(TransactionStatusEnum.PENDING),
                                AccountResultCode.ILLEGAL_STATUS, "Transaction status is illegal");

                        // or we need to query the transaction table where user id == userid and the status = OTP_OVER_LIMIT.
                        if (transactionRecord.getTxnStatus().equals(TransactionStatusEnum.OTP_OVER_LIMIT)) {
                            UpdateTransactionRecordRequest updateTransactionRecordRequest = new UpdateTransactionRecordRequest();
                            updateTransactionRecordRequest.setTxnId(transactionRecord.getTxnId());
                            updateTransactionRecordRequest.setStatus(TransactionStatusEnum.PENDING.getCode());
                            TransactionRecord updatedTransactionRecord = accountTransactionRepository
                                    .updateTransactionRecord(updateTransactionRecordRequest);
                            AssertUtil.notNull(updatedTransactionRecord, AccountResultCode.SYSTEM_EXCEPTION, "Transaction update failed");
                        }
                        // convert money to big decimal
                        BigDecimal amount = BigDecimal.valueOf(transactionRecord.getAmount().doubleValue())
                                .setScale(2, RoundingMode.HALF_UP);
                        // publish EC_TRANSACTION event code for transfer service to listen
                        EcTransactionEvent event = new EcTransactionEvent(
                                transactionRecord.getTxnId(),
                                transactionRecord.getPayeeAccountId(),
                                transactionRecord.getPayerAccountId(),
                                amount
                        );

                        // Use accountId as key → guarantees ordering per account
                        kafkaTemplate.send("EC_TRANSACTION", event.getPayeeAccountNo(), event);

                    }
                });
    }

    /**
     * validate user is authorised
     * @param operatorId
     */
    private static void validateUser(String operatorId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getPrincipal() == null) {
            throw new AccessDeniedException("Unauthenticated request");
        }

        String currentUserId = auth.getPrincipal().toString();

        if (!operatorId.equals(currentUserId)) {
            throw new AccessDeniedException("Unauthorized");
        }
    }
}
