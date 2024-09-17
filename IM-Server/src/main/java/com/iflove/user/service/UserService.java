package com.iflove.user.service;

import com.iflove.common.domain.vo.response.RestBean;
import com.iflove.user.domain.entity.User;
import com.iflove.user.domain.vo.response.user.UserInfoVO;

/**
* @author IFLOVE
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-09-17 14:57:36
*/
public interface UserService {
    RestBean<UserInfoVO> login(String username, String password);
    RestBean<Void> logout(String token);
}
