package com.iflove.common.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 群聊解散事件
 */
@Getter
public class GroupDismissedEvent extends ApplicationEvent {
    private final Long roomId;
    public GroupDismissedEvent(Object source, Long roomId) {
        super(source);
        this.roomId = roomId;
    }
}
