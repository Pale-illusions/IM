package com.iflove.api.chat.service.impl;

import com.iflove.api.chat.dao.MessageDao;
import com.iflove.api.chat.domain.entity.Message;
import com.iflove.api.chat.domain.vo.request.ChatMessageReq;
import com.iflove.api.chat.service.ChatService;
import com.iflove.api.chat.service.adapter.MessageAdapter;
import com.iflove.common.event.MessageSendEvent;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Service
public class ChatServiceImpl implements ChatService {
    @Resource
    ApplicationEventPublisher applicationEventPublisher;
    @Resource
    MessageDao messageDao;


    /**
     * 发送消息
     * @param request 消息请求体
     * @param uid 发送人id
     * @return {@link Long}
     */
    @Override
    public Long sendMsg(ChatMessageReq request, Long uid) {
        // TODO 单聊 -> 好友关系校验 / 群聊 -> 群成员校验

        // TODO 处理不同类型的消息的业务逻辑

        // TODO 简单文本消息处理，后续需更改逻辑，增加敏感词过滤功能
        Message message = MessageAdapter.buildMessageSave(request, uid);
        message.setContent(request.getBody().toString());
        messageDao.save(message);

        // 发送消息发送事件
        applicationEventPublisher.publishEvent(new MessageSendEvent(this, message.getId()));
        return message.getId();
    }
}
