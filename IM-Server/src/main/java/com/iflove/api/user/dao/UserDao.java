package com.iflove.api.user.dao;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflove.api.user.domain.entity.User;
import com.iflove.api.user.domain.enums.ChatActiveStatusEnum;
import com.iflove.api.user.mapper.UserMapper;
import com.iflove.common.domain.vo.request.CursorPageBaseReq;
import com.iflove.common.domain.vo.response.CursorPageBaseResp;
import com.iflove.common.utils.CursorUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author IFLOVE
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-09-17 14:57:36
*/
@Service
public class UserDao extends ServiceImpl<UserMapper, User> {
    public User getUserByName(String name) {
        return lambdaQuery()
                .eq(User::getName, name)
                .one();
    }

    public List<User> getFriendList(List<Long> friendIds) {
        return lambdaQuery()
                .in(User::getId, friendIds)
                .select(User::getId, User::getStatus, User::getName, User::getAvatar)
                .list();
    }

    public CursorPageBaseResp<User> getCursorPage(List<Long> memberUidList, CursorPageBaseReq cursorPageBaseReq, ChatActiveStatusEnum chatActiveStatusEnum) {
        return CursorUtils.getCursorPageByMysql(this, cursorPageBaseReq, wrapper -> {
            wrapper.eq(User::getStatus, chatActiveStatusEnum.getStatus());
            wrapper.in(CollectionUtil.isNotEmpty(memberUidList), User::getId, memberUidList);
        }, User::getLastOptTime);
    }

    public Long getOnlineCount(List<Long> memberUidList) {
        return lambdaQuery()
                .eq(User::getStatus, ChatActiveStatusEnum.ONLINE.getStatus())
                .in(CollectionUtil.isNotEmpty(memberUidList), User::getId, memberUidList)
                .count();
    }
}




