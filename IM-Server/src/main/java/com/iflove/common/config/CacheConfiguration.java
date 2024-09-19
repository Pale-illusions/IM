package com.iflove.common.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 缓存配置类
 */
@EnableCaching
@Configuration
public class CacheConfiguration extends CachingConfigurerSupport {
    @Bean("caffeineCacheManager")
    @Primary
    public CacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        // 定制化缓存Cache
        cacheManager.setCaffeine(Caffeine.newBuilder()
                // 缓存有效时间
                .expireAfterWrite(5, TimeUnit.MINUTES)
                // 初始缓存大小
                .initialCapacity(100)
                // 缓存最大容量
                .maximumSize(200));
        return cacheManager;
    }
}
