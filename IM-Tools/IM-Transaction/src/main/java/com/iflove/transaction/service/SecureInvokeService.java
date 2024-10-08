package com.iflove.transaction.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.iflove.transaction.dao.SecureInvokeRecordDao;
import com.iflove.transaction.domain.dto.SecureInvokeDTO;
import com.iflove.transaction.domain.entity.SecureInvokeRecord;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import utils.JsonUtil;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 安全执行处理器
 */
@Slf4j
@AllArgsConstructor
public class SecureInvokeService {
    // 重试间隔：2分钟
    public static final double RETRY_INTERVAL_MINUTES = 2D;

    private final SecureInvokeRecordDao secureInvokeRecordDao;

    private final Executor executor;

    /**
     * 定时重试 ( 每5秒执行一次 )
     */
    @Scheduled(cron = "*/5 * * * * ?")
    public void retry() {
        List<SecureInvokeRecord> secureInvokeRecords = secureInvokeRecordDao.getWaitRetryRecords();
        for (SecureInvokeRecord secureInvokeRecord : secureInvokeRecords) {
            doAsyncInvoke(secureInvokeRecord);
        }
    }

    /**
     * 重试，并保证事务状态一致性
     * @param record 记录
     * @param async 是否异步
     */
    public void invoke(SecureInvokeRecord record, boolean async) {
        boolean inTransaction = TransactionSynchronizationManager.isActualTransactionActive();
        // 非事务状态，直接执行，不做任何保证
        if (!inTransaction) {
            return;
        }
        // 保存执行数据
        secureInvokeRecordDao.save(record);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @SneakyThrows
            @Override
            public void afterCommit() {
                // 事务后执行
                if (async) {
                    doAsyncInvoke(record);
                } else {
                    doInvoke(record);
                }
            }
        });
    }

    /**
     * 异步执行重试
     * @param record 执行记录
     */
    public void doAsyncInvoke(SecureInvokeRecord record) {
        executor.execute(() -> {
            System.out.println(Thread.currentThread().getName());
            doInvoke(record);
        });
    }

    /**
     * 重试
     * @param record 执行记录
     */
    public void doInvoke(SecureInvokeRecord record) {
        SecureInvokeDTO secureInvokeDTO = record.getSecureInvokeDTO();
        try {
            SecureInvokeHolder.setInvoking();
            Class<?> beanClass = Class.forName(secureInvokeDTO.getClassName());
            Object bean = SpringUtil.getBean(beanClass);
            List<String> parameterStrings = JSONUtil.toList(secureInvokeDTO.getParameterTypes(), String.class);
            List<Class<?>> parameterClasses = getParameters(parameterStrings);
            Method method = ReflectUtil.getMethod(beanClass, secureInvokeDTO.getMethodName(), parameterClasses.toArray(new Class[]{}));
            Object[] args = getArgs(secureInvokeDTO, parameterClasses);
            // 执行方法
            method.invoke(bean, args);
            // 执行成功，更新状态
            removeRecord(record.getId());
        } catch (Throwable e) {
            log.error("SecureInvokeService invoke fail", e);
            //执行失败，等待下次执行
            retryRecord(record, e.getMessage());
        } finally {
            SecureInvokeHolder.invoked();
        }
    }

    /**
     * 执行失败，更新失败记录，等待重试
     * @param record 记录
     * @param message 错误信息
     */
    private void retryRecord(SecureInvokeRecord record, String message) {
        Integer retryTimes = record.getRetryTimes() + 1;
        SecureInvokeRecord update = new SecureInvokeRecord();
        update.setId(record.getId());
        update.setFailReason(message);
        update.setNextRetryTime(getNextRetryTime(retryTimes));
        if (retryTimes > record.getMaxRetryTimes()) {
            update.setStatus(SecureInvokeRecord.STATUS_FAIL);
        } else {
            update.setRetryTimes(retryTimes);
        }
        secureInvokeRecordDao.updateById(update);
    }

    /**
     * 指数上升算法获得下一次重试时间
     * @param retryTimes 已重试次数
     * @return 下一次重试时间
     */
    private Date getNextRetryTime(Integer retryTimes) {
        double waitMinutes = Math.pow(RETRY_INTERVAL_MINUTES, retryTimes);
        return DateUtil.offsetMinute(new Date(), (int) waitMinutes);
    }

    /**
     * 执行成功，移除记录
     * @param id 记录id
     */
    private void removeRecord(Long id) {
        secureInvokeRecordDao.removeById(id);
    }

    /**
     * 获取参数
     * @param secureInvokeDTO 安全执行数据
     * @param parameterClasses 参数类型
     * @return 参数集合
     */
    @NotNull
    private Object[] getArgs(SecureInvokeDTO secureInvokeDTO, List<Class<?>> parameterClasses) {
        JsonNode jsonNode = JsonUtil.toJsonNode(secureInvokeDTO.getArgs());
        Object[] args = new Object[jsonNode.size()];
        for (int i = 0; i < jsonNode.size(); i++) {
            Class<?> aClass = parameterClasses.get(i);
            args[i] = JsonUtil.nodeToValue(jsonNode.get(i), aClass);
        }
        return args;
    }

    /**
     * 获取参数类型
     * @param parameterStrings 参数类型字符串
     * @return 参数类型Class
     */
    @NotNull
    private List<Class<?>> getParameters(List<String> parameterStrings) {
        return parameterStrings
                .stream()
                .map(name -> {
                    try {
                        return Class.forName(name);
                    } catch (ClassNotFoundException e) {
                        log.error("SecureInvokeService class not fund", e);
                    }
                    return null;
                })
                .collect(Collectors.toList());
    }
}
