package com.alipay.account_center.common.service.facade.result;

import com.alipay.account_center.common.service.facade.baseresult.AccountBaseResult;
import com.alipay.account_center.common.service.facade.item.MetricCard;

import java.math.BigDecimal;

/**
 * @author adam
 * @date 24/6/2026 10:12 PM
 */
public class QueryAverageBasketResult extends AccountBaseResult {
    private MetricCard<Double> averageBasket;

    public MetricCard<Double> getAverageBasket() {
        return averageBasket;
    }

    public void setAverageBasket(MetricCard<Double> averageBasket) {
        this.averageBasket = averageBasket;
    }
}