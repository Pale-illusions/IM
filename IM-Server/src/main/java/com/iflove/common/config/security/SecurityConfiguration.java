package com.iflove.common.config.security;

import cn.hutool.json.JSONUtil;
import com.iflove.common.domain.vo.response.RestBean;
import com.iflove.common.exception.CommonErrorEnum;
import com.iflove.common.exception.UserErrorEnum;
import com.iflove.common.security.JwtAuthenticationProvider;
import com.iflove.common.security.JwtAuthenticationTokenFilter;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import utils.JsonUtil;

import java.io.IOException;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider();
    }
    // 自定义拦截器
    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter();
    }

    @Resource
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(conf -> conf
                        // 对登录注册允许匿名访问
                        .requestMatchers("/api/user/auth/**").permitAll()
                        // 放行 Knife4j 静态资源
                        .requestMatchers("/doc.html", "/webjars/**", "/swagger-resources/**", "/v2/api-docs/**", "/v3/api-docs/**", "/favicon.ico").permitAll()
                        // 除上面外的所有请求全部需要鉴权认证
                        .anyRequest().authenticated()
                )
                // 异常处理
                .exceptionHandling(conf -> conf
                        .accessDeniedHandler(this::onAccessDeny)
                        .authenticationEntryPoint(this::onUnauthorized)
                )
                // 禁用缓存
                .headers(headersConfigurer -> headersConfigurer
                        .cacheControl(HeadersConfigurer.CacheControlConfig::disable)
                )
                // 基于 token 不需要 csrf 防护
                .csrf(CsrfConfigurer::disable)
                // 基于token，所以不需要session
                .sessionManagement(conf -> conf
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 添加 JWT filter
                .addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                // 使用自定义 Provider
                .authenticationProvider(jwtAuthenticationProvider())
                .build();
    }

    private void onUnauthorized(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(JsonUtil.toStr(RestBean.failure(UserErrorEnum.UNAUTHENTICATED)));
    }

    private void onAccessDeny(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(JsonUtil.toStr(RestBean.failure(UserErrorEnum.FORBIDDEN)));
    }
}
