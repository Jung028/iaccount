package com.alipay.account_center.common.service.facade.request;

import com.alipay.account_center.common.service.facade.baseresult.AccountBaseRequest;

public class QueryPageBaseRequest extends AccountBaseRequest {
    private int pageNo = 1;
    private int pageSize = 10;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
