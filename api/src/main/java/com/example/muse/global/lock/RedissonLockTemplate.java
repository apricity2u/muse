package com.example.muse.global.lock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedissonLockTemplate {
    private final RedissonClient redissonClient;
    private static final long LOCK_WAIT_MS = 500;
    private static final long LOCK_LEASE_MS = 5000;

    public <T> T executeWithLock(String lockKey, Supplier<T> action) {

        RLock lock = redissonClient.getLock(lockKey);
        boolean locked = false;
        try {
            locked = lock.tryLock(LOCK_WAIT_MS, LOCK_LEASE_MS, TimeUnit.MILLISECONDS);
            if (!locked) {
                throw new RuntimeException("락 획득 실패: " + lockKey);
            }

            return action.get();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("락 대기 중 인터럽트 발생", e);

        } finally {
            if (locked && lock.isHeldByCurrentThread()) {
                try {
                    lock.unlock();
                } catch (IllegalMonitorStateException ignored) {
                }
            }
        }
    }
}