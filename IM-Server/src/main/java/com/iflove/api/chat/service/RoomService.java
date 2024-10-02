package com.iflove.api.chat.service;


import com.iflove.api.chat.domain.entity.RoomFriend;
import com.iflove.api.chat.domain.entity.RoomGroup;
import com.iflove.api.chat.domain.vo.response.ChatRoomResp;
import com.iflove.common.domain.vo.response.RestBean;

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

    /**
     * 获取好友房间
     * @param uid 用户id
     * @param friendId 好友id
     * @return 好友房间
     */
    RoomFriend getFriendRoom(Long uid, Long friendId);

    /**
     * 创建群聊
     * @param uid 用户id
     * @return 群聊
     */
    RoomGroup createRoomGroup(Long uid);
}
