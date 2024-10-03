package com.iflove.api.chat.service;

import com.iflove.api.chat.domain.vo.request.admin.AdminAddReq;
import com.iflove.api.chat.domain.vo.request.admin.AdminRevokeReq;
import com.iflove.api.chat.domain.vo.request.member.GroupCreateReq;
import com.iflove.api.chat.domain.vo.request.member.MemberAddReq;
import com.iflove.api.chat.domain.vo.request.member.MemberDelReq;
import com.iflove.api.chat.domain.vo.request.member.MemberPageReq;
import com.iflove.api.chat.domain.vo.response.ChatMemberResp;
import com.iflove.api.chat.domain.vo.response.ChatRoomResp;
import com.iflove.api.chat.domain.vo.response.GroupInfoResp;
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
     * @param uid 用户id
     * @return {@link RestBean}<{@link CursorPageBaseResp}<{@link ChatRoomResp}
     */
    RestBean<CursorPageBaseResp<ChatRoomResp>> getContactPage(CursorPageBaseReq req, Long uid);

    /**
     * 会话详情(房间id)
     * @param uid 用户id
     * @param roomId 房间id
     * @return {@link RestBean}<{@link ChatRoomResp}
     */
    RestBean<ChatRoomResp> getContactDetail(Long uid, Long roomId);

    /**
     * 会话详情(好友id)
     * @param uid 用户id
     * @param friendId 好友id
     * @return {@link RestBean}<{@link ChatRoomResp}
     */
    RestBean<ChatRoomResp> getContactDetailByFriend(Long uid, Long friendId);

    /**
     * 创建群聊
     * @param req 创建请求
     * @param uid 用户id
     * @return {@link RestBean}<{@link Long}
     */
    RestBean<Long> createGroup(GroupCreateReq req, Long uid);

    /**
     * 邀请成员
     * @param req 邀请成员请求体
     * @param uid 用户id
     * @return {@link RestBean}
     */
    RestBean<Void> addMember(MemberAddReq req, Long uid);

    /**
     * 删除成员
     * @param req 删除成员请求体
     * @param uid 用户id
     * @return {@link RestBean}
     */
    RestBean<Void> delMember(MemberDelReq req, Long uid);

    /**
     * 成员列表
     * @param req 成员列表请求体
     * @return {@link RestBean}<{@link CursorPageBaseResp}<{@link ChatMemberResp}
     */
    RestBean<CursorPageBaseResp<ChatMemberResp>> getMemberPage(MemberPageReq req);

    /**
     * 添加管理员
     * @param uid 用户id
     * @param req 添加管理员请求体
     * @return {@link RestBean}
     */
    RestBean<Void> addAdmin(Long uid, AdminAddReq req);

    /**
     * 撤销管理员
     * @param uid 用户id
     * @param req 撤销管理员请求体
     * @return {@link RestBean}
     */
    RestBean<Void> revokeAdmin(Long uid, AdminRevokeReq req);

    /**
     * 群组详情
     * @param uid 用户id
     * @param roomId 房间id
     * @return {@link RestBean}<{@link GroupInfoResp}
     */
    RestBean<GroupInfoResp> getGroupDetail(Long uid, Long roomId);
}
