package com.alipay.account_center.core.model.context;

import com.alipay.account_center.core.model.enums.AccountActionEnum;

import java.util.Date;

public class IdigitalriskContext {

    private static final long serialVersionUID = 1L;

    private Date time;
    private AccountActionEnum action;
    private String operatorId;
    private String operatorName;


    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public AccountActionEnum getAction() {
        return action;
    }

    public void setAction(AccountActionEnum action) {
        this.action = action;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public IdigitalriskContext(AccountActionEnum action, Date time, String operatorId, String operatorName) {
        this.action = action;
        this.time = time;
        this.operatorId = operatorId;
        this.operatorName = operatorName;
    }
}
