package com.alipay.account_center.biz.service.impl.transaction.factory;

import com.alipay.account_center.biz.service.impl.transaction.handler.TransactionalHandler;
import com.alipay.account_center.common.service.facade.enums.AccountResultCode;
import com.alipay.account_center.common.service.facade.enums.TransactionType;
import com.alipay.account_center.core.model.util.AssertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author adam
 * @date 7/4/2026 6:07 PM
 */
@Component
public class TransactionHandlerFactory {

    private final Map<TransactionType, TransactionalHandler> processorMap;

    @Autowired
    public TransactionHandlerFactory(List<TransactionalHandler> handlers) {
        this.processorMap = handlers.stream()
                .collect(Collectors.toMap(
                        TransactionalHandler::getType,
                        Function.identity()
                ));
    }

    public TransactionalHandler getHandler(String transactionType) {
        AssertUtil.notBlank(transactionType, AccountResultCode.PARAM_ILLEGAL, "transaction type cannot be blank");
        TransactionalHandler handler = processorMap.get(TransactionType.valueOf(transactionType));
        AssertUtil.notNull(handler, AccountResultCode.PARAM_ILLEGAL, "handler cannot be null");
        return handler;
    }
}
