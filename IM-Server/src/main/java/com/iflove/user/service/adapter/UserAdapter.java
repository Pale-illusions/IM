package com.iflove.user.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import com.iflove.user.domain.entity.User;
import com.iflove.user.domain.vo.response.user.UserInfoResp;
import com.iflove.user.domain.vo.response.user.UserLoginInfoResp;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Slf4j
public class UserAdapter {

    public static UserLoginInfoResp buildUserLoginInfoResp(User user, Consumer<UserLoginInfoResp> consumer) {
        UserLoginInfoResp vo = new UserLoginInfoResp();
        BeanUtil.copyProperties(user, vo);
        consumer.accept(vo);
        return vo;
    }

    public static UserInfoResp buildUserInfoResp(User user) {
        UserInfoResp resp = new UserInfoResp();
        BeanUtil.copyProperties(user, resp);
        return resp;
    }
}
