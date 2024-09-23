package com.iflove.api.user.controller;

import com.iflove.api.user.service.UserService;
import com.iflove.common.domain.vo.response.RestBean;
import com.iflove.common.utils.RequestHolder;
import com.iflove.api.user.domain.vo.response.user.UserInfoResp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
     * @return {@link RestBean}
     */
    @GetMapping("logout")
    @Operation(summary = "用户登出",
            description = "通过传递 Authorization 头中的 token 完成用户登出操作",
            security = {@SecurityRequirement(name = "Authorization")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "10001", description = "未授权或token无效"),
            @ApiResponse(responseCode = "10003", description = "请勿重复操作"),
            @ApiResponse(responseCode = "501", description = "服务器内部错误")
    })
    public RestBean<Void> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return userService.logout(token);
    }

    /**
     * 重置密码
     * @param password 新密码
     * @return {@link RestBean}
     */
    @PostMapping("reset")
    @Operation(summary = "重置密码",
            description = "用户通过提供新密码来重置密码，密码长度应为 6 到 20 个字符",
            security = {@SecurityRequirement(name = "Authorization")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "10001", description = "未授权或token无效"),
            @ApiResponse(responseCode = "10004", description = "用户不存在"),
            @ApiResponse(responseCode = "502", description = "参数校验失败"),
            @ApiResponse(responseCode = "501", description = "服务器内部错误")
    })
    public RestBean<Void> reset(@Length(min = 6, max = 20) String password) {
        return userService.reset(password);
    }

    /**
     * 获取自己用户信息
     * @return {@link RestBean}<{@link UserInfoResp}
     */
    @GetMapping("/userinfo/me")
    @Operation(summary = "获取当前用户信息",
            description = "获取登录用户的详细信息",
            security = {@SecurityRequirement(name = "Authorization")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "10001", description = "未授权或token无效"),
            @ApiResponse(responseCode = "501", description = "服务器内部错误")
    })
    public RestBean<UserInfoResp> getUserInfo() {
        return userService.getUserInfo(RequestHolder.get().getUid());
    }

    /**
     * 获取他人信息
     * @param id 用户id
     * @return {@link RestBean}<{@link UserInfoResp}
     */
    @GetMapping("/userinfo/{id}")
    @Operation(summary = "获取他人用户信息",
            description = "根据用户ID获取指定用户的详细信息",
            security = {@SecurityRequirement(name = "Authorization")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "10001", description = "未授权或token无效"),
            @ApiResponse(responseCode = "10004", description = "用户不存在"),
            @ApiResponse(responseCode = "502", description = "参数校验失败"),
            @ApiResponse(responseCode = "501", description = "服务器内部错误")
    })
    public RestBean<UserInfoResp> getUserInfo(@PathVariable @Min(1) Long id) {
        return userService.getUserInfo(id);
    }

    /**
     * 上传头像
     * @param url 头像下载链接
     * @return {@link RestBean}
     */
    @PostMapping("uploadAvatar")
    @Operation(summary = "上传头像",
            description = "用户通过提供头像的下载链接来更新头像信息",
            security = {@SecurityRequirement(name = "Authorization")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "10001", description = "未授权或token无效"),
            @ApiResponse(responseCode = "502", description = "参数校验失败"),
            @ApiResponse(responseCode = "501", description = "服务器内部错误")
    })
    public RestBean<Void> uploadAvatar(String url) {
        return userService.uploadAvatar(url, RequestHolder.get().getUid());
    }
}
