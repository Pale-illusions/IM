package com.iflove.api.chat.service.impl;

import com.iflove.api.chat.dao.*;
import com.iflove.api.chat.domain.entity.GroupMember;
import com.iflove.api.chat.domain.entity.Room;
import com.iflove.api.chat.domain.entity.RoomGroup;
import com.iflove.api.chat.domain.enums.GroupRoleEnum;
import com.iflove.api.chat.domain.vo.request.admin.AdminAddReq;
import com.iflove.api.chat.domain.vo.request.admin.AdminRevokeReq;
import com.iflove.api.chat.service.GroupMemberService;
import com.iflove.api.chat.service.cache.GroupMemberCache;
import com.iflove.api.chat.service.cache.RoomCache;
import com.iflove.api.chat.service.cache.RoomGroupCache;
import com.iflove.common.domain.vo.response.RestBean;
import com.iflove.common.event.GroupDismissedEvent;
import com.iflove.common.event.GroupMemberDelEvent;
import com.iflove.common.exception.RoomErrorEnum;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Service
public class GroupMemberServiceImpl implements GroupMemberService {
    @Resource
    private RoomGroupCache roomGroupCache;
    @Resource
    private GroupMemberDao groupMemberDao;
    @Resource
    private RoomCache roomCache;
    @Resource
    private GroupMemberCache groupMemberCache;
    @Resource
    private ContactDao contactDao;
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;
    @Resource
    private MessageDao messageDao;
    @Resource
    private RoomGroupDao roomGroupDao;
    @Resource
    private RoomDao roomDao;

    /**
     * 添加管理员
     * @param uid 用户id
     * @param req 添加管理员请求体
     * @return {@link RestBean}
     */
    @Override
    @Transactional
    public RestBean<Void> addAdmin(Long uid, AdminAddReq req) {
        RoomGroup roomGroup = roomGroupCache.get(req.getRoomId());
        // 房间不存在
        if (Objects.isNull(roomGroup)) return RestBean.failure(RoomErrorEnum.ROOM_NOT_EXIST);
        // 判断用户是否在群中
        GroupMember member = groupMemberDao.getMember(roomGroup.getId(), uid);
        if (Objects.isNull(member)) return RestBean.failure(RoomErrorEnum.MEMBER_NOT_EXIST);
        // 判断用户是否为群主
        if (!isLeader(member)) return RestBean.failure(RoomErrorEnum.PERMISSION_NOT_GRANTED);
        // 添加管理员
        groupMemberDao.addAdmin(roomGroup.getId(), req.getUidList());
        return RestBean.success();
    }

    /**
     * 撤销管理员
     * @param uid 用户id
     * @param req 撤销管理员请求体
     * @return {@link RestBean}
     */
    @Override
    @Transactional
    public RestBean<Void> revokeAdmin(Long uid, AdminRevokeReq req) {
        RoomGroup roomGroup = roomGroupCache.get(req.getRoomId());
        // 房间不存在
        if (Objects.isNull(roomGroup)) return RestBean.failure(RoomErrorEnum.ROOM_NOT_EXIST);
        // 判断用户是否在群中
        GroupMember member = groupMemberDao.getMember(roomGroup.getId(), uid);
        if (Objects.isNull(member)) return RestBean.failure(RoomErrorEnum.MEMBER_NOT_EXIST);
        // 判断用户是否为群主
        if (!isLeader(member)) return RestBean.failure(RoomErrorEnum.PERMISSION_NOT_GRANTED);
        // 撤销管理员
        groupMemberDao.revokeAdmin(roomGroup.getId(), req.getUidList());
        return RestBean.success();
    }

    /**
     * 退出群聊
     * @param roomId 房间id
     * @param uid 用户id
     * @return {@link RestBean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestBean<Void> exitGroup(Long roomId, Long uid) {
        // TODO 群成员数量小于等于2 直接解散群聊
        Room room = roomCache.get(roomId);
        RoomGroup roomGroup = roomGroupCache.get(roomId);
        // 1. 房间不存在
        if (Objects.isNull(roomGroup) || Objects.isNull(room)) return RestBean.failure(RoomErrorEnum.ROOM_NOT_EXIST);

        GroupMember member = groupMemberDao.getMember(roomGroup.getId(), uid);
        // 2. 不是群成员
        if (Objects.isNull(member)) return RestBean.failure(RoomErrorEnum.NOT_IN_GROUP);

        // 3. 判断是否为群主
        if (isLeader(member)) {
            // 4.1 如果是群主, 解散群聊
            // 4.2 删除消息记录
            messageDao.removeByRoomId(roomId, Collections.emptyList());
            // 4.3 删除会话
            contactDao.removeByRoomId(roomId, Collections.emptyList());
            // 4.4 删除群成员
            groupMemberDao.removeByGroupId(roomGroup.getId(), Collections.emptyList());
            // 4.5 删除 群聊 房间
            roomGroupDao.removeById(roomGroup.getId());
            // 4.6 删除 房间
            roomDao.removeById(roomId);
            // 4.7 发布 群聊解散 事件
            applicationEventPublisher.publishEvent(new GroupDismissedEvent(this, roomId));
        } else {
            // 5.1 不是群主, 正常退出
            // 5.2 删除会话
            contactDao.removeByRoomId(roomId, Collections.singletonList(uid));
            // 5.3 删除群成员
            groupMemberDao.removeByGroupId(roomGroup.getId(), Collections.singletonList(uid));
            // 5.4 发布 成员移除 事件
            applicationEventPublisher.publishEvent(new GroupMemberDelEvent(this, uid, roomGroup));
        }
        return RestBean.success();
    }

    /**
     * 判断是否为群主
     */
    private boolean isLeader(GroupMember member) {
        return Objects.equals(member.getRole(), GroupRoleEnum.LEADER.getType());
    }
}
