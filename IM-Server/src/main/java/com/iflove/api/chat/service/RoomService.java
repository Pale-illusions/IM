package com.iflove.api.chat.service;


import com.iflove.api.chat.domain.entity.RoomFriend;

import java.util.List;

/**
* @author IFLOVE
* @description 针对表【room(房间表)】的数据库操作Service
* @createDate 2024-09-17 14:57:36
*/
public interface RoomService {

    /**
     * 创建一个单聊房间
     * @param uidList 好友uid
     * @return {@link RoomFriend}
     */
    RoomFriend createRoomFriend(List<Long> uidList);

    /**
     * 禁用一个单聊房间
     * @param uidList 好友uid
     */
    void disableRoomFriend(List<Long> uidList);
}
