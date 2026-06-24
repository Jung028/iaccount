package com.alipay.account_center.common.dal.auto.dataobject;

/**
 * Holds aggregated revenue and count from a single SQL query over the transaction table.
 */
public class TransactionMetricsDTO {
    private long revenue;
    private int count;

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
