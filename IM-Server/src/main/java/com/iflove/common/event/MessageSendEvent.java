package com.iflove.common.event;

import com.iflove.chat.domain.entity.Message;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 消息发送事件
 */
@Getter
public class MessageSendEvent extends ApplicationEvent {
    private final Long msgId;
    public MessageSendEvent(Object source, Long msgId) {
        super(source);
        this.msgId = msgId;
    }
}
