package com.iflove.user.service.impl;

import cn.hutool.jwt.JWTUtil;
import com.iflove.common.constant.Const;
import com.iflove.common.constant.RedisKey;
import com.iflove.common.domain.vo.response.RestBean;
import com.iflove.common.exception.CommonErrorEnum;
import com.iflove.common.exception.ErrorEnum;
import com.iflove.common.exception.LoginErrorEnum;
import com.iflove.user.dao.UserDao;
import com.iflove.user.domain.entity.User;
import com.iflove.user.domain.vo.request.user.UserRegisterVO;
import com.iflove.user.domain.vo.response.user.UserInfoVO;
import com.iflove.user.service.UserService;
import com.iflove.user.service.adapter.UserAdapter;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utils.RedisUtil;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    UserDao userDao;
    @Resource
    AuthenticationManager authenticationManager;
    @Resource
    PasswordEncoder passwordEncoder;

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 用户信息结果集
     */
    @Override
    public RestBean<UserInfoVO> login(String username, String password) {
        //AuthenticationManager authenticate进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authentication)) {
            throw new BadCredentialsException("验证失败");
        }
        // 获取token
        String uuid = UUID.randomUUID().toString();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, Const.EXPIRE_TIME);
        Date expireTime = calendar.getTime();
        Map<String, Object> map = Map.of("jwt_id", uuid, "username", username, "expire_time", expireTime);
        String token = JWTUtil.createToken(map, Const.JWT_SIGN_KEY.getBytes());
        // 将token存入redis
        RedisUtil.set(RedisKey.getKey(RedisKey.JWT_WHITE_LIST, uuid), "", Const.EXPIRE_TIME, TimeUnit.HOURS);
        // 返回结果集
        return RestBean.success(UserAdapter.buildUserInfoVO(userDao.getUserByName(username), v -> {
            v.setToken(token);
            v.setExpireTime(expireTime);
        }));
    }

    /**
     * 用户登出
     * @param token token
     * @return 结果集
     */
    @Override
    public RestBean<Void> logout(String token) {
        String jwtId = (String) JWTUtil.parseToken(token).getPayload("jwt_id");
        // 禁止重复登出
        if (RedisUtil.hasKey(RedisKey.getKey(RedisKey.JWT_BLACK_LIST, jwtId))) return RestBean.failure(LoginErrorEnum.FREQUENT_LOGOUT_ERROR);
        // 登出
        if (RedisUtil.set(RedisKey.getKey(RedisKey.JWT_BLACK_LIST, jwtId), "")) {
            return RestBean.success();
        } else {
            return RestBean.failure(CommonErrorEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 用户注册
     * @param userRegisterVO 注册信息
     * @return
     */
    @Transactional
    @Override
    public RestBean<Void> register(UserRegisterVO userRegisterVO) {
        User user = User.builder()
                .name(userRegisterVO.getUsername())
                .password(passwordEncoder.encode(userRegisterVO.getPassword()))
                .sex(userRegisterVO.getSex())
                .build();
        return userDao.save(user) ?
                RestBean.success() : RestBean.failure(CommonErrorEnum.SYSTEM_ERROR);
    }
}
