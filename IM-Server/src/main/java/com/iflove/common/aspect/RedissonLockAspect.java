package com.iflove.common.aspect;

import com.iflove.common.annotation.RedissonLock;
import com.iflove.common.service.LockService;
import com.iflove.common.utils.SPELUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 分布式锁切面
 */
@Aspect
@Component
@Order(0)
public class RedissonLockAspect {
    @Resource
    private LockService lockService;

    @Around("@annotation(redissonLock)")
    public Object around(ProceedingJoinPoint joinPoint, RedissonLock redissonLock) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        // 默认使用方法全限定名
        String prefix = StringUtils.isBlank(redissonLock.prefixKey()) ? SPELUtils.getMethodKey(method) : redissonLock.prefixKey();
        String key = SPELUtils.parseSPEL(method, joinPoint.getArgs(), redissonLock.key());
        return lockService.executeWithLockThrows(prefix + ":" + key, redissonLock.waitTime(), redissonLock.unit(), joinPoint::proceed);
    }
}
