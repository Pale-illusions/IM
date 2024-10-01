package com.iflove.common.config.thread;

import com.iflove.transaction.annotation.SecureInvokeConfigurer;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 线程池配置
 */
@Configuration
@EnableAsync
public class ThreadPoolConfiguration implements AsyncConfigurer, SecureInvokeConfigurer {
    // 项目共用线程池
    public static final String IM_EXECUTOR = "IMExecutor";
    // websocket 通信线程池
    public static final String WS_EXECUTOR = "websocketExecutor";

    @Override
    public Executor getAsyncExecutor() {
        return IMExecutor();
    }

    @Override
    public Executor getSecureInvokeExecutor() {
        return IMExecutor();
    }

    @Bean(IM_EXECUTOR)
    @Primary
    public ThreadPoolTaskExecutor IMExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // 核心线程数
        executor.setMaxPoolSize(10); // 最大线程数
        executor.setQueueCapacity(200); // 任务队列容量
        // 拒绝策略，任务队列满时，任务由调用线程（提交任务的线程）直接执行，保证重要任务不会被丢弃。
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadFactory(new CustomizableThreadFactory("IM-Thread-")); // 线程工厂
        executor.setWaitForTasksToCompleteOnShutdown(true); // 等待任务执行完毕
        executor.setAwaitTerminationSeconds(60); // 最多等待60秒
        executor.initialize();
        return executor;
    }

    @Bean(WS_EXECUTOR)
    public ThreadPoolTaskExecutor websocketExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(16); // 核心线程数
        executor.setMaxPoolSize(16); // 最大线程数
        executor.setQueueCapacity(1000); //支持同时推送1000人
        // 拒绝策略为丢弃策略（DiscardPolicy），当任务队列已满时直接丢弃新任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        executor.setThreadFactory(new CustomizableThreadFactory("Websocket-Thread-")); // 线程工厂
        executor.setWaitForTasksToCompleteOnShutdown(true); // 等待任务执行完毕
        executor.setAwaitTerminationSeconds(60); // 最多等待60秒
        executor.initialize();
        return executor;
    }
}
