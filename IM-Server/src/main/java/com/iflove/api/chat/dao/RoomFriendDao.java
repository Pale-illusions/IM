package com.iflove.api.chat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflove.api.chat.domain.entity.RoomFriend;
import com.iflove.api.chat.mapper.RoomFriendMapper;
import com.iflove.common.domain.enums.NormalOrNoEnum;
import org.springframework.stereotype.Service;

/**
* @author IFLOVE
* @description 针对表【room_friend(单聊表)】的数据库操作Service实现
* @createDate 2024-09-17 14:57:36
*/
@Service
public class RoomFriendDao extends ServiceImpl<RoomFriendMapper, RoomFriend> {

    public RoomFriend getByKey(String key) {
        return lambdaQuery()
                .eq(RoomFriend::getRoomKey, key)
                .one();
    }

    public void restoreRoom(Long id) {
        lambdaUpdate()
                .set(RoomFriend::getStatus, NormalOrNoEnum.NORMAL.getStatus())
                .eq(RoomFriend::getId, id)
                .update();
    }

    public RoomFriend getByRoomId(Long id) {
        return lambdaQuery()
                .eq(RoomFriend::getRoomId, id)
                .select(RoomFriend::getUserId1, RoomFriend::getUserId2)
                .one();
    }

    public void disableRoomFriend(String key) {
        lambdaUpdate()
                .set(RoomFriend::getStatus, NormalOrNoEnum.FORBIDDEN.getStatus())
                .eq(RoomFriend::getRoomKey, key)
                .update();
    }
}




