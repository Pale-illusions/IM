package com.iflove.common.event.listener;

import com.iflove.api.chat.domain.entity.Room;
import com.iflove.api.chat.domain.entity.RoomGroup;
import com.iflove.api.chat.domain.vo.request.msg.ChatMessageReq;
import com.iflove.api.chat.service.ChatService;
import com.iflove.api.chat.service.adapter.RoomAdapter;
import com.iflove.api.chat.service.cache.GroupMemberCache;
import com.iflove.api.chat.service.cache.RoomCache;
import com.iflove.api.chat.service.cache.RoomGroupCache;
import com.iflove.api.user.domain.entity.User;
import com.iflove.api.user.domain.vo.response.ws.WSBaseResp;
import com.iflove.api.user.domain.vo.response.ws.WSGroupDismissedResp;
import com.iflove.api.user.service.PushService;
import com.iflove.common.event.GroupDismissedEvent;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 群聊解散事件监听器
 */
@Component
public class GroupDismissedListener {
    @Resource
    private GroupMemberCache groupMemberCache;
    @Resource
    private PushService pushService;
    @Resource
    private RoomGroupCache roomGroupCache;

    @Async
    @TransactionalEventListener(classes = GroupDismissedEvent.class, fallbackExecution = true)
    public void sendGroupDismissedMsg(GroupDismissedEvent event) {
        Long roomId = event.getRoomId();
        List<Long> memberUidList = groupMemberCache.getMemberUidList(roomId);
        RoomGroup roomGroup = roomGroupCache.get(roomId);
        WSBaseResp<WSGroupDismissedResp> ws = RoomAdapter.buildGroupDismissedResp(roomGroup);
        pushService.sendPushMsg(ws, memberUidList);
        groupMemberCache.evictMemberUidList(roomId);
    }
}
