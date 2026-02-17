package com.alipay.alipay_plus.biz.service.impl.lock;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class DistributedLockImpl implements DistributedLock {

    private static final long WAIT_LOOP_MILLIS = 100;

    private final RedisTemplate<String, String> redisTemplate;

    // Each thread remembers its own lock value
    private final ThreadLocal<String> lockValueHolder = new ThreadLocal<>();

    public DistributedLockImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean tryLock(String lockKey, long ttlMillis) {
        if (lockKey == null || lockKey.isEmpty()) {
            return false;
        }

        String lockValue = UUID.randomUUID().toString();

        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, lockValue, ttlMillis, TimeUnit.MILLISECONDS);

        if (Boolean.TRUE.equals(success)) {
            lockValueHolder.set(lockValue);
            return true;
        }
        return false;
    }

    @Override
    public boolean tryLockAndWait(String lockKey, long ttlMillis, long waitMillis) {
        long remaining = waitMillis;

        while (remaining > 0) {
            if (tryLock(lockKey, ttlMillis)) {
                return true;
            }

            try {
                Thread.sleep(WAIT_LOOP_MILLIS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }

            remaining -= WAIT_LOOP_MILLIS;
        }
        return false;
    }

    @Override
    public boolean unlock(String lockKey) {
        if (lockKey == null || lockKey.isEmpty()) {
            return false;
        }

        String lockValue = lockValueHolder.get();
        if (lockValue == null) {
            return false;
        }

        String luaScript =
                "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                        "   return redis.call('del', KEYS[1]) " +
                        "else " +
                        "   return 0 " +
                        "end";

        Long result = redisTemplate.execute(
                new DefaultRedisScript<>(luaScript, Long.class),
                Collections.singletonList(lockKey),
                lockValue
        );

        lockValueHolder.remove();
        return result != null && result > 0;
    }
}