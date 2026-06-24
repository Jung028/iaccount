package com.alipay.account_center.common.service.facade.item;

/**
 * @author adam
 * @date 24/6/2026 10:02 PM
 */
public class MetricCard<T> {
    private T data;
    private double percentageChange;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public double getPercentageChange() {
        return percentageChange;
    }

    public void setPercentageChange(double percentageChange) {
        this.percentageChange = percentageChange;
    }
}