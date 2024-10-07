package com.iflove.common.service;

import com.iflove.common.exception.BusinessException;
import com.iflove.common.exception.CommonErrorEnum;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 分布式锁服务类
 */
@Service
public class LockService {
    @Resource
    private RedissonClient redissonClient;

    public <T> T executeWithLockThrows(String key, int waitTime, TimeUnit unit, SupplierThrow<T> supplier) throws Throwable {
        RLock lock = redissonClient.getLock(key);
        boolean lockSuccess = lock.tryLock(waitTime, unit);
        if (!lockSuccess) {
            throw new BusinessException(CommonErrorEnum.LOCK_LIMIT);
        }
        try {
            return supplier.get();
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread())
                lock.unlock();
        }
    }

    @FunctionalInterface
    public interface SupplierThrow<T> {
        T get() throws Throwable;
    }
}
