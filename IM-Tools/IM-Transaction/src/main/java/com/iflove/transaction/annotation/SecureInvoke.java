package com.iflove.transaction.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 安全执行注解
 */
@Retention(RetentionPolicy.RUNTIME) // 运行时生效
@Target(ElementType.METHOD) // 作用于方法上
public @interface SecureInvoke {

    /**
     * 定义最大重试次数，默认3次
     * @return 最大重试次数
     */
    int maxRetryTimes() default 3;

    /**
     * 是否开启异步执行，默认开启
     * @return 是否开启异步执行
     */
    boolean async() default true;
}
