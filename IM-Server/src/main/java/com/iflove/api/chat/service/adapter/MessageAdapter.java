package com.iflove.api.chat.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import com.iflove.api.chat.domain.entity.Message;
import com.iflove.api.chat.domain.entity.msg.TextMsgDTO;
import com.iflove.api.chat.domain.enums.MessageStatusEnum;
import com.iflove.api.chat.domain.enums.MessageTypeEnum;
import com.iflove.api.chat.domain.vo.request.ChatMessageReq;
import com.iflove.api.chat.domain.vo.response.ChatMessageResp;
import com.iflove.api.chat.service.strategy.AbstractMsgHandler;
import com.iflove.api.chat.service.strategy.MsgHandlerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
public class MessageAdapter {

    public static Message buildMessageSave(ChatMessageReq req, Long uid) {
        return Message
                .builder()
                .fromUid(uid)
                .roomId(req.getRoomId())
                .type(req.getMsgType())
                .status(MessageStatusEnum.NORMAL.getStatus())
                .build();
    }

    public static ChatMessageReq buildApplyApproveMsg(Long roomId) {
        return ChatMessageReq
                .builder()
                .msgType(MessageTypeEnum.TEXT.getType())
                .roomId(roomId)
                .body(TextMsgDTO
                        .builder()
                        .content("我们已经成为好友了，开始聊天吧")
                        .build()
                )
                .build();
    }

    public static Message buildMsgSave(ChatMessageReq req, Long uid) {
        return Message
                .builder()
                .fromUid(uid)
                .roomId(req.getRoomId())
                .type(req.getMsgType())
                .status(MessageStatusEnum.NORMAL.getStatus())
                .build();
    }

    public static List<ChatMessageResp> buildMessageResp(List<Message> messages) {
        return messages
                .stream()
                .map(message -> {
                    return ChatMessageResp
                            .builder()
                            .fromUser(buildUserInfo(message.getFromUid()))
                            .message(buildMessage(message))
                            .build();
                })
                // 根据创建时间排序，保证消息时序性
                .sorted(Comparator.comparing(a -> a.getMessage().getSendTime()))
                .collect(Collectors.toList());
    }

    private static ChatMessageResp.Message buildMessage(Message message) {
        ChatMessageResp.Message vo = new ChatMessageResp.Message();
        BeanUtil.copyProperties(message, vo);
        vo.setSendTime(message.getCreateTime());
        AbstractMsgHandler<?> abstractMsgHandler = MsgHandlerFactory.getStrategyNonNull(message.getType());
        vo.setBody(abstractMsgHandler.showMsg(message));
        return vo;
    }

    private static ChatMessageResp.UserInfo buildUserInfo(Long id) {
        ChatMessageResp.UserInfo userInfo = new ChatMessageResp.UserInfo();
        userInfo.setUid(id);
        return userInfo;
    }
}
