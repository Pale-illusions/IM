package com.iflove.transaction.annotation;

import jakarta.annotation.Nullable;

import java.util.concurrent.Executor;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 线程池配置
 */
public interface SecureInvokeConfigurer {

    /**
     * 返回一个线程池
     * @return 线程池
     */
    @Nullable
    default Executor getSecureInvokeExecutor() {
        return null;
    }
}
