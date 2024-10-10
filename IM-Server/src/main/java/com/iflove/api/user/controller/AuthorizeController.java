package com.iflove.api.user.controller;

import com.iflove.api.user.domain.entity.IpInfo;
import com.iflove.api.user.domain.vo.response.user.UserLoginInfoResp;
import com.iflove.api.user.service.UserService;
import com.iflove.common.domain.vo.response.RestBean;
import com.iflove.api.user.domain.vo.request.user.UserRegisterReq;
import com.iflove.common.utils.IPUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.Length;
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
@Tag(name = "用户权限模块")
public class AuthorizeController {
    @Resource
    UserService userService;

    /**
     * 用户登录
     * @param name 用户名
     * @param password 密码
     * @return {@link RestBean}<{@link UserLoginInfoResp}
     */
    @PostMapping("login")
    @Operation(summary = "登录", description = "用户登录", security = {})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "502", description = "参数校验失败"),
            @ApiResponse(responseCode = "501", description = "服务器内部错误")
    })
    public RestBean<UserLoginInfoResp> login(@RequestParam("name") @Length(min = 1, max = 20) String name,
                                             @RequestParam("password") @Length(min = 6, max = 20) String password,
                                             HttpServletRequest request) {
        String ip = IPUtils.getIp(request);
        String cityInfo = IPUtils.getCityInfo(ip);
        IpInfo ipInfo = IpInfo.init(cityInfo);
        return userService.login(name, password, ipInfo);
    }

    /**
     * 用户注册
     * @param userRegisterReq 注册信息
     * @return {@link RestBean}
     */
    @PostMapping("register")
    @Operation(summary = "注册", description = "用户注册", security = {})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "502", description = "参数校验失败"),
            @ApiResponse(responseCode = "501", description = "服务器内部错误")
    })
    public RestBean<Void> register(@RequestBody @Valid UserRegisterReq userRegisterReq) {
        return userService.register(userRegisterReq);
    }
}
