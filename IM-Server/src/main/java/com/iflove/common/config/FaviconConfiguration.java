package com.iflove.common.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */

@SpringBootConfiguration
public class FaviconConfiguration implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
                if (!"GET".equalsIgnoreCase(request.getMethod()) || !request.getRequestURI().toString().equals("/favicon.ico")) {
                    return true;
                }
                response.setStatus(HttpStatus.NO_CONTENT.value()); // 设置状态码为204 No Content
                return false;
            }
        }).addPathPatterns("/**");
    }
}