package com.alipay.account_center.common.service.facade.pair;

/**
 * @author adam
 * @date 7/4/2026 8:25 PM
 */
public class LockPair {
    private final String firstLock;
    private final String secondLock;

    public LockPair(String firstLock, String secondLock) {
        this.firstLock = firstLock;
        this.secondLock = secondLock;
    }

    public String getFirstLock() {
        return firstLock;
    }

    public String getSecondLock() {
        return secondLock;
    }
}