package com.iflove.api.chat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.iflove.api.chat.dao.*;
import com.iflove.api.chat.domain.entity.*;
import com.iflove.api.chat.domain.vo.request.msg.ChatMessagePageReq;
import com.iflove.api.chat.domain.vo.request.msg.ChatMessageReq;
import com.iflove.api.chat.domain.vo.response.ChatMessageResp;
import com.iflove.api.chat.service.ChatService;
import com.iflove.api.chat.service.adapter.MessageAdapter;
import com.iflove.api.chat.service.cache.*;
import com.iflove.api.chat.service.strategy.AbstractMsgHandler;
import com.iflove.api.chat.service.strategy.MsgHandlerFactory;
import com.iflove.api.user.domain.entity.User;
import com.iflove.common.domain.enums.NormalOrNoEnum;
import com.iflove.common.domain.vo.response.CursorPageBaseResp;
import com.iflove.common.domain.vo.response.RestBean;
import com.iflove.common.event.MessageSendEvent;
import com.iflove.common.exception.RoomErrorEnum;
import jakarta.annotation.Resource;
import jakarta.validation.ValidationException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Service
public class ChatServiceImpl implements ChatService {
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;
    @Resource
    private MessageDao messageDao;
    @Resource
    private RoomCache roomCache;
    @Resource
    private RoomFriendCache roomFriendCache;
    @Resource
    private RoomGroupCache roomGroupCache;
    @Resource
    private ContactDao contactDao;
    @Resource
    private GroupMemberDao groupMemberDao;
    @Resource
    private RoomFriendDao roomFriendDao;
    @Resource
    private RoomGroupDao roomGroupDao;

    /**
     * 发送消息
     * @param request 消息请求体
     * @param uid 发送人id
     * @return {@link Long}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long sendMsg(ChatMessageReq request, Long uid) {
        // 单聊 -> 好友关系校验 / 群聊 -> 群成员校验
        checkRoom(request.getRoomId(), uid);
        // 处理不同类型的消息
        AbstractMsgHandler<?> msgHandler = MsgHandlerFactory.getStrategyNonNull(request.getMsgType());
        Long msgId = msgHandler.checkAndSaveMsg(request, uid);
        // 发送消息发送事件
        applicationEventPublisher.publishEvent(new MessageSendEvent(this, msgId));
        return msgId;
    }

    private void checkRoom(Long roomId, Long uid) {
        Room room = roomCache.get(roomId);
        if (Objects.isNull(room)) throw new ValidationException("房间不存在");
        // 单聊处理逻辑
        if (room.isRoomFriend()) {
            RoomFriend roomFriend = roomFriendCache.get(room.getId());
            // 房间被禁用
            if (Objects.equals(roomFriend.getStatus(), NormalOrNoEnum.FORBIDDEN.getStatus())) {
                throw new ValidationException("您和对方还不是好友");
            }
            // 判断 uid 是否一致
            if (!Objects.equals(uid, roomFriend.getUserId1()) && !Objects.equals(uid, roomFriend.getUserId2())) {
                throw new ValidationException("您和对方还不是好友");
            }
        }
        // 群聊处理逻辑
        if (room.isRoomGroup()) {
            RoomGroup roomGroup = roomGroupCache.get(roomId);
            GroupMember member = groupMemberDao.getMember(roomGroup.getId(), uid);
            // 保证系统消息正确发送
            if (Objects.isNull(member) && !Objects.equals(uid, User.SYSTEM_UID)) {
                throw new ValidationException("您不是群成员");
            }
        }
    }

    /**
     * 消息返回体
     * @param msgId 消息id
     * @return {@link ChatMessageResp}
     */
    @Override
    public ChatMessageResp getMsgResp(Long msgId) {
        Message message = messageDao.getById(msgId);
        return getMsgResp(message);
    }

    /**
     * 消息返回体
     * @param message 消息对象
     * @return {@link ChatMessageResp}
     */
    @Override
    public ChatMessageResp getMsgResp(Message message) {
        return CollectionUtil.getFirst(getMsgRespBatch(Collections.singletonList(message)));
    }

    /**
     * 批量获取消息返回体
     * @param messages 消息集合
     * @return {@link List}<{@link ChatMessageResp}
     */
    private List<ChatMessageResp> getMsgRespBatch(List<Message> messages) {
        if (CollectionUtil.isEmpty(messages)) {
            return new ArrayList<>();
        }
        return MessageAdapter.buildMessageResp(messages);
    }

    /**
     * 消息列表
     * @param req 消息列表游标分页请求
     * @param uid 用户id
     * @return {@link RestBean}<{@link CursorPageBaseResp}<{@link ChatMessageResp}
     */
    @Override
    public RestBean<CursorPageBaseResp<ChatMessageResp>> getMsgPage(ChatMessagePageReq req, Long uid) {
        // 单聊 -> 好友关系校验 / 群聊 -> 群成员校验
        checkRoom(req.getRoomId(), uid);
        CursorPageBaseResp<Message> cursorPage = messageDao.getCursorpage(req.getRoomId(), req);
        if (cursorPage.isEmpty()) {
            return RestBean.success(CursorPageBaseResp.empty());
        }
        return RestBean.success(CursorPageBaseResp.init(cursorPage, getMsgRespBatch(cursorPage.getList())));
    }

    /**
     * 消息阅读上报
     * @param roomId 房间id
     * @param uid 用户id
     * @return {@link RestBean}
     */
    @Override
    @Transactional
    public RestBean<Void> msgRead(Long roomId, Long uid) {
        // 检查房间是否存在
        Room room = roomCache.get(roomId);
        if (Objects.isNull(room)) return RestBean.failure(RoomErrorEnum.ROOM_NOT_EXIST);
        Contact contact = contactDao.get(uid, roomId);
        // 存在会话, 更新阅读时间
        if (Objects.nonNull(contact)) {
            contactDao.updateById(Contact.builder()
                    .id(contact.getId())
                    .readTime(new Date())
                    .build());
        } else {
            // 单聊校验
            RoomFriend roomFriend = roomFriendDao.getByRoomId(roomId);
            if (!Objects.equals(uid, roomFriend.getUserId1()) && !Objects.equals(uid, roomFriend.getUserId2())) {
                return RestBean.failure(RoomErrorEnum.NOT_IN_GROUP);
            }
            // 群聊校验
            RoomGroup roomGroup = roomGroupDao.getByRoomId(roomId);
            GroupMember member = groupMemberDao.getMember(roomGroup.getId(), uid);
            if (Objects.isNull(member)) {
                return RestBean.failure(RoomErrorEnum.NOT_IN_GROUP);
            }
            // 校验通过, 新建会话
            contactDao.save(Contact.builder()
                    .userId(uid)
                    .roomId(roomId)
                    .readTime(new Date())
                    .build());
        }
        return RestBean.success();
    }
}
