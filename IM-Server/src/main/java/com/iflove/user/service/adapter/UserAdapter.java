package com.iflove.user.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import com.iflove.user.domain.entity.User;
import com.iflove.user.domain.vo.response.user.UserInfoVO;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Slf4j
public class UserAdapter {

    public static UserInfoVO buildUserInfoVO(User user, Consumer<UserInfoVO> consumer) {
        UserInfoVO vo = new UserInfoVO();
        BeanUtil.copyProperties(user, vo);
        consumer.accept(vo);
        return vo;
    }
}
