package com.iflove.user.controller;

import com.iflove.common.domain.vo.response.RestBean;
import com.iflove.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@RestController
@RequestMapping("/api/user")
@Validated
@Tag(name = "用户模块")
public class UserController {
    @Resource
    UserService userService;

    /**
     * 用户登出
     * @param request http请求
     * @return 结果集
     */
    @GetMapping("logout")
    @Operation(summary = "登出")
    public RestBean<Void> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return userService.logout(token);
    }

    /**
     * 重置密码
     * @param password 新密码
     * @return 结果集
     */
    @PostMapping("reset")
    @Operation(summary = "重置密码")
    public RestBean<Void> reset(@Length(min = 6, max = 20) String password) {
        return userService.reset(password);
    }
}
