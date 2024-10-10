package com.iflove.api.user.service;

import com.iflove.api.user.domain.entity.IpInfo;
import com.iflove.common.domain.vo.response.RestBean;
import com.iflove.api.user.domain.vo.request.user.UserRegisterVO;
import com.iflove.api.user.domain.vo.response.user.UserInfoResp;
import com.iflove.api.user.domain.vo.response.user.UserLoginInfoResp;

/**
* @author IFLOVE
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-09-17 14:57:36
*/
public interface UserService {
    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return {@link RestBean}<{@link UserLoginInfoResp}
     */
    RestBean<UserLoginInfoResp> login(String username, String password, IpInfo ipInfo);

    /**
     * 用户登出
     * @param token token
     * @return {@link RestBean}
     */
    RestBean<Void> logout(String token);

    /**
     * 用户注册
     * @param userRegisterVO 注册信息
     * @return {@link RestBean}
     */
    RestBean<Void> register(UserRegisterVO userRegisterVO);

    /**
     * 重置密码
     * @param password 新密码
     * @return {@link RestBean}
     */
    RestBean<Void> reset(String password);

    /**
     * 获取用户信息
     * @param uid 用户id
     * @return {@link RestBean}<{@link UserInfoResp}
     */
    RestBean<UserInfoResp> getUserInfo(Long uid);

    /**
     * 上传头像
     * @param url 头像下载链接
     * @return {@link RestBean}
     */
    RestBean<Void> uploadAvatar(String url, Long uid);
}
