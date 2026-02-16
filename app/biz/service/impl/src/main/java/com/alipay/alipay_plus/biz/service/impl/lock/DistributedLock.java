package com.alipay.alipay_plus.biz.service.impl.lock;

public interface DistributedLock {

    boolean tryLock(String lockKey, long ttlMillis);
    boolean tryLockAndWait(String lockKey, long ttlMillis, long waitMillis);
    boolean unlock(String lockKey);
}
