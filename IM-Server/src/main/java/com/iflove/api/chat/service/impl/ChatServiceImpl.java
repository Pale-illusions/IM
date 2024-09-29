package com.iflove.api.chat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.iflove.api.chat.dao.MessageDao;
import com.iflove.api.chat.domain.entity.Message;
import com.iflove.api.chat.domain.entity.Room;
import com.iflove.api.chat.domain.entity.RoomFriend;
import com.iflove.api.chat.domain.vo.request.ChatMessageReq;
import com.iflove.api.chat.domain.vo.response.ChatMessageResp;
import com.iflove.api.chat.service.ChatService;
import com.iflove.api.chat.service.adapter.MessageAdapter;
import com.iflove.api.chat.service.cache.MsgCache;
import com.iflove.api.chat.service.cache.RoomCache;
import com.iflove.api.chat.service.cache.RoomFriendCache;
import com.iflove.api.chat.service.cache.RoomGroupCache;
import com.iflove.api.chat.service.strategy.AbstractMsgHandler;
import com.iflove.api.chat.service.strategy.MsgHandlerFactory;
import com.iflove.common.domain.enums.NormalOrNoEnum;
import com.iflove.common.event.MessageSendEvent;
import jakarta.annotation.Resource;
import jakarta.validation.ValidationException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
    private MsgCache msgCache;

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
        checkRoom(request, uid);
        // 处理不同类型的消息
        AbstractMsgHandler<?> msgHandler = MsgHandlerFactory.getStrategyNonNull(request.getMsgType());
        Long msgId = msgHandler.checkAndSaveMsg(request, uid);
        // 发送消息发送事件
        applicationEventPublisher.publishEvent(new MessageSendEvent(this, msgId));
        return msgId;
    }

    private void checkRoom(ChatMessageReq request, Long uid) {
        Room room = roomCache.get(request.getRoomId());
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
        // TODO 群聊处理逻辑
        if (room.isRoomGroup()) {

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

}
