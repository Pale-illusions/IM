package com.iflove.common.security;

import cn.hutool.jwt.JWTUtil;
import com.iflove.common.constant.Const;
import com.iflove.common.constant.RedisKey;
import com.iflove.user.service.cache.UserInfoCache;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import utils.RedisUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 过滤器
 */
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private static final String AUTH_HEADER = "Authorization";
    private static final String AUTH_HEADER_TYPE = "Bearer";
    @Resource
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 从请求头获取 token： Authorization: Bearer <token>
        String authHeader = request.getHeader(AUTH_HEADER);
        // 如果为空或不是 Bearer 令牌
        if (Objects.isNull(authHeader) || !authHeader.startsWith(AUTH_HEADER_TYPE)) {
            filterChain.doFilter(request, response);
            return;
        }
        // 去掉 Bearer 令牌
        String authToken = authHeader.split(" ")[1];
        log.info("authToken:{}", authToken);
        // 验证 token 的 signature 是否一致
        if (!JWTUtil.verify(authToken, Const.JWT_SIGN_KEY.getBytes(StandardCharsets.UTF_8))) {
            log.info("invalid token");
            filterChain.doFilter(request, response);
            return;
        }
        // 如果 该Token 被列入黑名单，拒绝访问 或者 Token 已过期
        String jwtId = (String) JWTUtil.parseToken(authToken).getPayload("jwt_id");
        if (RedisUtil.getStr(RedisKey.getKey(RedisKey.JWT_BLACK_LIST, jwtId)) != null
                || RedisUtil.getStr(RedisKey.getKey(RedisKey.JWT_WHITE_LIST, jwtId)) == null) {
            log.info("请重新登录");
            filterChain.doFilter(request, response);
            return;
        }
        // 获取 payload 中的 username
        final String username = (String) JWTUtil.parseToken(authToken).getPayload("username");
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // 注意，这里使用的是3个参数的构造方法，此构造方法将认证状态设置为true
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        //将认证过了凭证保存到security的上下文中以便于在程序中使用
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
