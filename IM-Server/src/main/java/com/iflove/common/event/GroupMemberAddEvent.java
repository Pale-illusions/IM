package com.iflove.common.event;

import com.iflove.api.chat.domain.entity.GroupMember;
import com.iflove.api.chat.domain.entity.RoomGroup;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 添加群聊事件
 */
@Getter
public class GroupMemberAddEvent extends ApplicationEvent {
    private final List<GroupMember> memberList;
    private final RoomGroup roomGroup;
    private final Long inviteUid;

    public GroupMemberAddEvent(Object source, List<GroupMember> memberList, RoomGroup roomGroup, Long inviteUid) {
        super(source);
        this.memberList = memberList;
        this.roomGroup = roomGroup;
        this.inviteUid = inviteUid;
    }
}
