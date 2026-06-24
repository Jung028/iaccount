package com.alipay.account_center.common.service.facade.result;

import com.alipay.account_center.common.service.facade.baseresult.AccountBaseResult;
import com.alipay.account_center.common.service.facade.item.MetricCard;

public class QueryNewCustomerCountResult extends AccountBaseResult {
    private MetricCard<Integer> customerCount;

    public MetricCard<Integer> getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(MetricCard<Integer> customerCount) {
        this.customerCount = customerCount;
    }
}
