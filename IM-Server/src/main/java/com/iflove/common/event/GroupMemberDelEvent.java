package com.iflove.common.event;

import com.iflove.api.chat.domain.entity.GroupMember;
import com.iflove.api.chat.domain.entity.RoomGroup;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 群成员移除事件
 */
@Getter
public class GroupMemberDelEvent extends ApplicationEvent {
    private final Long target;
    private final RoomGroup roomGroup;

    public GroupMemberDelEvent(Object source, Long target, RoomGroup roomGroup) {
        super(source);
        this.target = target;
        this.roomGroup = roomGroup;
    }
}
