package com.iflove.common.config.interceptor;

import com.iflove.common.interceptor.CollectorInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 拦截器配置类
 */
@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {
    @Resource
    private CollectorInterceptor collectorInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(collectorInterceptor)
                .addPathPatterns("/api/**");
    }
}
