package com.iflove.transaction.aspect;

import cn.hutool.core.date.DateUtil;
import com.iflove.transaction.annotation.SecureInvoke;
import com.iflove.transaction.domain.dto.SecureInvokeDTO;
import com.iflove.transaction.domain.entity.SecureInvokeRecord;
import com.iflove.transaction.service.SecureInvokeHolder;
import com.iflove.transaction.service.SecureInvokeService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import utils.JsonUtil;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 安全执行切面
 */
@Slf4j
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE  + 1) // 确保最先执行
@Component
public class SecureInvokeAspect {
    @Resource
    private SecureInvokeService secureInvokeService;

    /**
     * 安全执行切面 ( 环绕 )
     * @param joinPoint 切入点
     * @param secureInvoke 安全执行注解
     * @return 执行结果
     * @throws Throwable err
     */
    @Around("@annotation(secureInvoke)")
    public Object around(ProceedingJoinPoint joinPoint, SecureInvoke secureInvoke) throws Throwable {
        boolean async = secureInvoke.async();
        boolean inTransaction = TransactionSynchronizationManager.isActualTransactionActive();
        // 非事务状态，直接执行，不做任何保证
        if (SecureInvokeHolder.isInvoking() || !inTransaction) {
            return joinPoint.proceed();
        }
        Method method = ((MethodSignature) (joinPoint.getSignature())).getMethod();
        List<String> parameters = Stream.of(method.getParameterTypes()).map(Class::getName).collect(Collectors.toList());
        SecureInvokeDTO dto = SecureInvokeDTO.builder()
                .args(JsonUtil.toStr(joinPoint.getArgs()))
                .className(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(JsonUtil.toStr(parameters))
                .build();
        SecureInvokeRecord record = SecureInvokeRecord.builder()
                .secureInvokeDTO(dto)
                .maxRetryTimes(secureInvoke.maxRetryTimes())
                .nextRetryTime(DateUtil.offsetMinute(new Date(), (int) SecureInvokeService.RETRY_INTERVAL_MINUTES))
                .build();
        // 立即进行安全执行
        secureInvokeService.invoke(record, async);
        return null;
    }
}
