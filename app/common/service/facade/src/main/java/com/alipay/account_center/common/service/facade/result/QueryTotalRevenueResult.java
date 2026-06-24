package com.alipay.account_center.common.service.facade.result;

import com.alipay.account_center.common.service.facade.baseresult.AccountBaseResult;
import com.alipay.account_center.common.service.facade.item.MetricCard;

import java.math.BigDecimal;

/**
 * @author adam
 * @date 24/6/2026 4:36 PM
 */
public class QueryTotalRevenueResult extends AccountBaseResult {

    private MetricCard<BigDecimal> totalRevenue;

    public MetricCard<BigDecimal> getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(MetricCard<BigDecimal> totalRevenue) {
        this.totalRevenue = totalRevenue;
    }


}