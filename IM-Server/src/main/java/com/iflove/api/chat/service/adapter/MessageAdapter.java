package com.iflove.api.chat.service.adapter;

import com.iflove.api.chat.domain.entity.Message;
import com.iflove.api.chat.domain.enums.MessageStatusEnum;
import com.iflove.api.chat.domain.enums.MessageTypeEnum;
import com.iflove.api.chat.domain.vo.request.ChatMessageReq;
import com.iflove.api.chat.domain.vo.request.TextMsgReq;

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
                .body(TextMsgReq
                        .builder()
                        .content("我们已经成为好友了，开始聊天吧")
                        .build()
                )
                .build();
    }
}
