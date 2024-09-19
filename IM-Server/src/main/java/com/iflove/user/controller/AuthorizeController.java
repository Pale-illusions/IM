package com.iflove.user.controller;

import com.iflove.common.domain.vo.response.RestBean;
import com.iflove.user.domain.entity.User;
import com.iflove.user.domain.vo.request.user.UserRegisterVO;
import com.iflove.user.domain.vo.response.user.UserInfoVO;
import com.iflove.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
@RequestMapping("api/user/auth")
@Validated
@Tag(name = "用户权限接口")
public class AuthorizeController {
    @Resource
    UserService userService;

    /**
     * 用户登录
     * @param name 用户名
     * @param password 密码
     * @return 用户信息结果集
     */
    @PostMapping("login")
    @Operation(summary = "登录")
    public RestBean<UserInfoVO> login(@RequestParam("name") @Length(min = 1, max = 20) String name,
                                      @RequestParam("password") @Length(min = 6, max = 20) String password) {
        return userService.login(name, password);
    }

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

    @PostMapping("register")
    @Operation(summary = "注册")
    public RestBean<Void> register(@RequestBody @Valid UserRegisterVO userRegisterVO) {
        return userService.register(userRegisterVO);
    }
}
