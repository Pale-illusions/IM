package com.iflove.user.controller;

import com.iflove.common.domain.vo.response.RestBean;
import com.iflove.common.utils.RequestHolder;
import com.iflove.user.domain.vo.response.user.UserInfoResp;
import com.iflove.user.domain.vo.response.user.UserLoginInfoResp;
import com.iflove.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.Length;
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


    /**
     * 获取自己用户详情
     * @return 结果集
     */
    @GetMapping("/userinfo/me")
    @Operation(summary = "获取自己用户信息")
    public RestBean<UserInfoResp> getUserInfo() {
        return userService.getUserInfo(RequestHolder.get().getUid());
    }

    /**
     * 获取他人用户信息
     * @param id id
     * @return 结果集
     */
    @GetMapping("/userinfo/{id}")
    @Operation(summary = "获取他人用户信息")
    public RestBean<UserInfoResp> getUserInfo(@PathVariable @Min(1) Long id) {
        return userService.getUserInfo(id);
    }

    /**
     * 上传头像
     * @param url 头像下载链接
     * @return 结果集
     */
    @PostMapping("uploadAvatar")
    @Operation(summary = "上传头像")
    public RestBean<Void> uploadAvatar(String url) {
        return userService.uploadAvatar(url, RequestHolder.get().getUid());
    }
}
