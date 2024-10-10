package com.iflove.api.chat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflove.api.chat.domain.entity.RoomFriend;
import com.iflove.api.chat.mapper.RoomFriendMapper;
import com.iflove.common.domain.enums.NormalOrNoEnum;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author IFLOVE
* @description 针对表【room_friend(单聊表)】的数据库操作Service实现
* @createDate 2024-09-17 14:57:36
*/
@Service
public class RoomFriendDao extends ServiceImpl<RoomFriendMapper, RoomFriend> {

    /**
     * 根据key获取好友房间
     * @param key
     * @return
     */
    public RoomFriend getByKey(String key) {
        return lambdaQuery()
                .eq(RoomFriend::getRoomKey, key)
                .one();
    }

    /**
     * 恢复好友房间
     * @param id
     */
    public void restoreRoom(Long id) {
        lambdaUpdate()
                .set(RoomFriend::getStatus, NormalOrNoEnum.NORMAL.getStatus())
                .eq(RoomFriend::getId, id)
                .update();
    }

    /**
     * 根据roomId获取好友房间
     * @param id
     * @return
     */
    public RoomFriend getByRoomId(Long id) {
        return lambdaQuery()
                .eq(RoomFriend::getRoomId, id)
                .select(RoomFriend::getUserId1, RoomFriend::getUserId2)
                .one();
    }

    /**
     * 删除好友房间
     * @param key
     */
    public void disableRoomFriend(String key) {
        lambdaUpdate()
                .set(RoomFriend::getStatus, NormalOrNoEnum.FORBIDDEN.getStatus())
                .eq(RoomFriend::getRoomKey, key)
                .update();
    }

    /**
     * 好友房间列表
     * @param roomIds
     * @return
     */
    public List<RoomFriend> listByRoomIds(List<Long> roomIds) {
        return lambdaQuery()
                .in(RoomFriend::getRoomId, roomIds)
                .list();
    }
}




