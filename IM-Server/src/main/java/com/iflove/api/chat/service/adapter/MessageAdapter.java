package com.iflove.api.chat.service.adapter;

import com.iflove.api.chat.domain.entity.Message;
import com.iflove.api.chat.domain.enums.MessageStatusEnum;
import com.iflove.api.chat.domain.vo.request.ChatMessageReq;

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
}
