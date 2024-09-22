package com.iflove.api.user.service.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.jwt.JWTUtil;
import com.iflove.api.user.dao.UserDao;
import com.iflove.api.user.domain.vo.response.user.UserLoginInfoResp;
import com.iflove.api.user.service.UserService;
import com.iflove.api.user.service.adapter.UserAdapter;
import com.iflove.common.constant.Const;
import com.iflove.common.constant.RedisKey;
import com.iflove.common.domain.vo.response.RestBean;
import com.iflove.common.exception.CommonErrorEnum;
import com.iflove.common.exception.UserErrorEnum;
import com.iflove.common.utils.RequestHolder;
import com.iflove.api.user.domain.entity.User;
import com.iflove.api.user.domain.vo.request.user.UserRegisterVO;
import com.iflove.api.user.domain.vo.response.user.UserInfoResp;
import com.iflove.api.user.service.cache.UserInfoCache;
import jakarta.annotation.Resource;
import jakarta.validation.ValidationException;
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
    @Resource
    UserInfoCache userInfoCache;

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 用户信息结果集
     */
    @Override
    public RestBean<UserLoginInfoResp> login(String username, String password) {
        //AuthenticationManager authenticate进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authentication)) {
            throw new BadCredentialsException("验证失败");
        }
        User user = userDao.getUserByName(username);
        // 获取token
        String uuid = UUID.randomUUID().toString();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, Const.EXPIRE_TIME);
        Date expireTime = calendar.getTime();
        Map<String, Object> map = Map.of("jwt_id", uuid, "username", username, "expire_time", expireTime, "uid", user.getId());
        String token = JWTUtil.createToken(map, Const.JWT_SIGN_KEY.getBytes());
        // 将token存入redis
        RedisUtil.set(RedisKey.getKey(RedisKey.JWT_WHITE_LIST, uuid), "", Const.EXPIRE_TIME, TimeUnit.HOURS);
        // 返回结果集
        return RestBean.success(UserAdapter.buildUserLoginInfoResp(user, v -> {
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
        if (RedisUtil.hasKey(RedisKey.getKey(RedisKey.JWT_BLACK_LIST, jwtId))) return RestBean.failure(UserErrorEnum.FREQUENT_LOGOUT_ERROR);
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

    /**
     * 重置密码
     * @param password 密码
     * @return 结果集
     */
    @Transactional
    @Override
    public RestBean<Void> reset(String password) {
        User user = userDao.getById(RequestHolder.get().getUid());
        // 用户不存在
        if (Objects.isNull(user)) return RestBean.failure(UserErrorEnum.USER_NOT_FOUND);
        user.setPassword(passwordEncoder.encode(password));
        return userDao.updateById(user) ?
                RestBean.success() : RestBean.failure(CommonErrorEnum.SYSTEM_ERROR);
    }

    /**
     * 获取用户信息
     * @param uid 用户id
     * @return
     */
    @Override
    public RestBean<UserInfoResp> getUserInfo(Long uid) {
        User user = userInfoCache.get(uid);
        return Objects.nonNull(user) ?
                RestBean.success(UserAdapter.buildUserInfoResp(user)) : RestBean.failure(UserErrorEnum.USER_NOT_FOUND);
    }

    /**
     * 上传头像
     * @param url 头像下载链接
     * @return 结果集
     */
    @Transactional
    @Override
    public RestBean<Void> uploadAvatar(String url, Long uid) {
        boolean valid = Validator.isUrl(url);
        if (!valid) {
            throw new ValidationException("非法链接");
        }
        User user = new User();
        user.setId(uid);
        user.setAvatar(url);
        user.setUpdateTime(new Date());
        // 删除缓存
        userInfoCache.delete(uid);
        return userDao.updateById(user) ?
                RestBean.success() : RestBean.failure(CommonErrorEnum.SYSTEM_ERROR);
    }
}
