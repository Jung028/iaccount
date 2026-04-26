package com.alipay.account_center.common.service.facade.baseresult;

/**
 * @author adam
 * @date 15/3/2026 12:49 AM
 */
public class AccountPageBaseResult extends AccountBaseResult {
    private int totalCount;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}