package com.iflove.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 分布式锁注解
 */
@Retention(RetentionPolicy.RUNTIME) // 运行时生效
@Target(ElementType.METHOD) // 作用在方法上
public @interface RedissonLock {
    /**
     * key 前缀，默认取方法全限定名
     * @return key 前缀
     */
    String prefixKey() default "";

    /**
     * SPEL 表达式
     * @return 表达式
     */
    String key();

    /**
     * 等待时间，默认-1，不等待直接失败
     * @return 等待时间
     */
    int waitTime() default -1;

    /**
     * 等待锁的时间单位，默认毫秒
     * @return 单位
     */
    TimeUnit unit() default TimeUnit.MILLISECONDS;
}
