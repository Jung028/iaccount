package com.alipay.account_center.web;

import com.alipay.account_center.common.service.facade.api.AccountService;
import com.alipay.account_center.common.service.facade.baseresult.AccountBizResult;
import com.alipay.account_center.common.service.facade.item.AccountInfoItem;
import com.alipay.account_center.common.service.facade.item.TransactionRecordItem;
import com.alipay.account_center.common.service.facade.request.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account/basic")
public class AccountBasicController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/createAccount.json")
    public AccountBizResult<String> createAccount(
            @RequestBody CreateAccountRequest request) {
        return accountService.createAccount(request);
    }

    @PostMapping("/queryAccountInfo.json")
    public AccountBizResult<AccountInfoItem> queryAccountInfo(
            @RequestBody QueryAccountInfoRequest request) {
        return accountService.queryAccountInfo(request);
    }

    @PostMapping("/queryAccountInfoByUserId.json")
    public AccountBizResult<AccountInfoItem> queryAccountInfoByUserId(
            @RequestBody QueryAccountInfoRequest request) {
        return accountService.queryAccountInfoByUserId(request);
    }

    @PostMapping("/queryTransactionRecord.json")
    public AccountBizResult<TransactionRecordItem> queryTransactionRecord(
            @RequestBody QueryTransactionRecordRequest request) {
        return accountService.queryTransactionRecord(request);
    }

    @PostMapping("/queryTransactionByStatus.json")
    public AccountBizResult<TransactionRecordItem> queryTransactionByStatus(
            @RequestBody QueryTransactionRecordRequest request) {
        return accountService.queryTransactionByStatus(request);
    }

    @PostMapping("/queryTransactionHistory.json")
    public AccountBizResult<QueryTransactionHistoryResult> queryTransactionHistory(
            @RequestBody QueryTransactionHistoryRequest request) {
        return accountService.queryTransactionHistory(request);
    }

    @PostMapping("/insertTransactionRecord.json")
    public AccountBizResult<TransactionRecordItem> insertTransactionRecord(
            @RequestBody InsertTransactionRecordRequest request) {
        return accountService.insertTransactionRecord(request);
    }

    @PostMapping("/updateTransactionRecord.json")
    public AccountBizResult<TransactionRecordItem> updateTransactionRecord(
            @RequestBody UpdateTransactionRecordRequest request) {
        return accountService.updateTransactionRecord(request);
    }
}