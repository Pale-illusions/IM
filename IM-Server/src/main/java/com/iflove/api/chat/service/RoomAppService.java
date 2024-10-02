package com.iflove.api.chat.service;

import com.iflove.api.chat.domain.vo.response.ChatRoomResp;
import com.iflove.common.domain.vo.response.RestBean;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
public interface RoomAppService {

    /**
     * 会话详情(房间id)
     * @param roomId 房间id
     * @return {@link RestBean}<{@link ChatRoomResp}
     */
    RestBean<ChatRoomResp> getContactDetail(Long uid, Long roomId);

    /**
     * 会话详情(好友id)
     * @param friendId 好友id
     * @return {@link RestBean}<{@link ChatRoomResp}
     */
    RestBean<ChatRoomResp> getContactDetailByFriend(Long uid, Long friendId);
}
