package com.iflove.common.event.listener;

import com.iflove.api.chat.domain.entity.GroupMember;
import com.iflove.api.chat.domain.entity.RoomGroup;
import com.iflove.api.chat.service.adapter.MemberAdapter;
import com.iflove.api.chat.service.cache.GroupMemberCache;
import com.iflove.api.user.domain.vo.response.ws.WSBaseResp;
import com.iflove.api.user.domain.vo.response.ws.WSMemberChange;
import com.iflove.api.user.service.PushService;
import com.iflove.common.event.GroupMemberDelEvent;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 群成员移除事件
 */
@Component
public class GroupMemberDelListener {
    @Resource
    private PushService pushService;
    @Resource
    private GroupMemberCache groupMemberCache;

    @Async
    @TransactionalEventListener(classes = GroupMemberDelEvent.class, fallbackExecution = true)
    public void sendChangePush(GroupMemberDelEvent event) {
        RoomGroup roomGroup = event.getRoomGroup();
        Long target = event.getTarget();
        WSBaseResp<WSMemberChange> wsBaseResp = MemberAdapter.buildMemberDelWS(roomGroup.getRoomId(), target);
        List<Long> memberUidList = groupMemberCache.getMemberUidList(roomGroup.getRoomId());
        // 发送 消息
        pushService.sendPushMsg(wsBaseResp, memberUidList);
        // 移除缓存
        groupMemberCache.evictMemberUidList(roomGroup.getRoomId());
    }
}
