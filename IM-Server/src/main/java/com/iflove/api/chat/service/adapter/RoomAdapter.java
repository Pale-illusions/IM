package com.iflove.api.chat.service.adapter;

import com.iflove.api.chat.domain.entity.GroupMember;
import com.iflove.api.chat.domain.entity.RoomGroup;
import com.iflove.api.chat.domain.entity.msg.SystemMsgDTO;
import com.iflove.api.chat.domain.enums.GroupRoleEnum;
import com.iflove.api.chat.domain.enums.MessageTypeEnum;
import com.iflove.api.chat.domain.vo.request.msg.ChatMessageReq;
import com.iflove.api.user.domain.entity.User;
import com.iflove.api.user.domain.enums.WSRespTypeEnum;
import com.iflove.api.user.domain.vo.response.ws.WSBaseResp;
import com.iflove.api.user.domain.vo.response.ws.WSGroupDismissedResp;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
public class RoomAdapter {

    public static List<GroupMember> buildGroupMemberBatch(List<Long> uidList, Long groupId) {
        return uidList.stream()
                .distinct()
                .map(uid -> {
                    GroupMember member = new GroupMember();
                    member.setRole(GroupRoleEnum.MEMBER.getType());
                    member.setUserId(uid);
                    member.setGroupId(groupId);
                    return member;
                }).collect(Collectors.toList());
    }

    public static ChatMessageReq buildGroupAddMessage(RoomGroup roomGroup, User user, Map<Long, User> batch) {
        // 构建消息体
        String invitedUsers = batch.values()
                .stream()
                .map(User::getName)
                .collect(Collectors.joining(", "));
        String messageBody = String.format("%s 邀请 %s 加入群聊", user.getName(), invitedUsers);

        return ChatMessageReq.builder()
                .roomId(roomGroup.getRoomId())
                .msgType(MessageTypeEnum.SYSTEM.getType())
                .body(new SystemMsgDTO(messageBody))
                .build();
    }

    public static WSBaseResp<WSGroupDismissedResp> buildGroupDismissedResp(RoomGroup roomGroup) {
        WSBaseResp<WSGroupDismissedResp> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(WSRespTypeEnum.GROUP_DISMISSED.getType());
        wsBaseResp.setData(WSGroupDismissedResp.builder()
                .roomId(roomGroup.getRoomId())
                .content("群聊 " + roomGroup.getName() + " 已解散")
                .optTime(new Date())
                .build()
        );
        return wsBaseResp;
    }
}
