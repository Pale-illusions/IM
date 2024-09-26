package com.iflove.api.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflove.api.user.domain.entity.UserFriend;
import com.iflove.api.user.mapper.UserFriendMapper;
import com.iflove.common.domain.vo.request.CursorPageBaseReq;
import com.iflove.common.domain.vo.response.CursorPageBaseResp;
import com.iflove.common.utils.CursorUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author IFLOVE
* @description 针对表【user_friend(用户好友表)】的数据库操作Service实现
* @createDate 2024-09-17 14:57:36
*/
@Service
public class UserFriendDao extends ServiceImpl<UserFriendMapper, UserFriend> {

    public UserFriend getByFriend(Long uid, Long targetUid) {
        return lambdaQuery()
                .eq(UserFriend::getUserId, uid)
                .eq(UserFriend::getFriendId, targetUid)
                .one();
    }

    public List<UserFriend> getUserFriend(Long uid, Long targetUid) {
        return lambdaQuery()
                .eq(UserFriend::getUserId, uid)
                .eq(UserFriend::getFriendId, targetUid)
                .or()
                .eq(UserFriend::getFriendId, uid)
                .eq(UserFriend::getUserId, targetUid)
                .list();
    }

    public CursorPageBaseResp<UserFriend> getFriendPage(Long uid, CursorPageBaseReq req) {
        return CursorUtils.getCursorPageByMysql(this, req,
                wrapper -> wrapper.eq(UserFriend::getUserId, uid), UserFriend::getId);
    }

    public List<UserFriend> getByFriends(Long uid, List<Long> uidList) {
        return lambdaQuery()
                .eq(UserFriend::getUserId, uid)
                .in(UserFriend::getFriendId, uidList)
                .list();
    }
}




