package com.iflove.api.chat.service;

import com.iflove.api.chat.domain.vo.request.GroupCreateReq;
import com.iflove.api.chat.domain.vo.request.MemberAddReq;
import com.iflove.api.chat.domain.vo.request.MemberDelReq;
import com.iflove.api.chat.domain.vo.response.ChatRoomResp;
import com.iflove.common.domain.vo.request.CursorPageBaseReq;
import com.iflove.common.domain.vo.response.CursorPageBaseResp;
import com.iflove.common.domain.vo.response.RestBean;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
public interface RoomAppService {
    /**
     * 会话列表
     * @param req 游标分页请求
     * @return {@link RestBean}<{@link CursorPageBaseResp}<{@link ChatRoomResp}
     */
    RestBean<CursorPageBaseResp<ChatRoomResp>> getContactPage(CursorPageBaseReq req, Long uid);

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

    /**
     * 创建群聊
     * @param req 创建请求
     * @return {@link RestBean}<{@link Long}
     */
    RestBean<Long> createGroup(GroupCreateReq req, Long uid);

    /**
     * 邀请成员
     * @param req 邀请成员请求体
     * @return {@link RestBean}
     */
    RestBean<Void> addMember(MemberAddReq req, Long uid);

    /**
     * 删除成员
     * @param req 删除成员请求体
     * @return {@link RestBean}
     */
    RestBean<Void> delMember(MemberDelReq req, Long uid);
}
