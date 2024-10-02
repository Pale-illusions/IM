package com.iflove.common.event.listener;

import com.iflove.api.chat.domain.entity.GroupMember;
import com.iflove.api.chat.domain.entity.RoomGroup;
import com.iflove.api.chat.domain.vo.request.ChatMessageReq;
import com.iflove.api.chat.service.ChatService;
import com.iflove.api.chat.service.adapter.MemberAdapter;
import com.iflove.api.chat.service.adapter.RoomAdapter;
import com.iflove.api.chat.service.cache.GroupMemberCache;
import com.iflove.api.user.dao.UserDao;
import com.iflove.api.user.domain.entity.User;
import com.iflove.api.user.domain.vo.response.ws.WSBaseResp;
import com.iflove.api.user.domain.vo.response.ws.WSMemberChange;
import com.iflove.api.user.service.PushService;
import com.iflove.api.user.service.cache.UserInfoCache;
import com.iflove.common.event.GroupMemberAddEvent;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 添加群聊监听器
 */
@Component
public class GroupMemberAddListener {
    @Resource
    private UserInfoCache userInfoCache;
    @Resource
    private ChatService chatService;
    @Resource
    private PushService pushService;
    @Resource
    private GroupMemberCache groupMemberCache;
    @Resource
    private UserDao userDao;

    /**
     * 系统统一发送群组新增成员消息
     * @param event 添加群聊事件
     */
    @Async
    @TransactionalEventListener(classes = GroupMemberAddEvent.class, fallbackExecution = true)
    public void sendAddMsg(GroupMemberAddEvent event) {
        List<GroupMember> memberList = event.getMemberList();
        RoomGroup roomGroup = event.getRoomGroup();
        Long inviteUid = event.getInviteUid();
        User user = userInfoCache.get(inviteUid);
        List<Long> uidList = memberList.stream().map(GroupMember::getUserId).collect(Collectors.toList());
        ChatMessageReq chatMessageReq = RoomAdapter.buildGroupAddMessage(roomGroup, user, userInfoCache.getBatch(uidList));
        chatService.sendMsg(chatMessageReq, User.SYSTEM_UID);
    }

    /**
     * 推送实时websocket消息，通知群成员新用户添加
     * @param event 添加群聊事件
     */
    @Async
    @TransactionalEventListener(classes = GroupMemberAddEvent.class, fallbackExecution = true)
    public void sendChangePush(GroupMemberAddEvent event) {
        List<GroupMember> memberList = event.getMemberList();
        RoomGroup roomGroup = event.getRoomGroup();
        List<Long> memberUidList = groupMemberCache.getMemberUidList(roomGroup.getRoomId());
        List<Long> uidList = memberList.stream().map(GroupMember::getUserId).collect(Collectors.toList());
        List<User> users = userDao.listByIds(uidList);
        users.forEach(user -> {
            WSBaseResp<WSMemberChange> ws = MemberAdapter.buildMemberAddWS(roomGroup.getRoomId(), user);
            pushService.sendPushMsg(ws, memberUidList);
        });
        // 移除缓存
        groupMemberCache.evictMemberUidList(roomGroup.getRoomId());
    }
}
