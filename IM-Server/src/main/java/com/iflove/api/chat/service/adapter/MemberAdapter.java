package com.iflove.api.chat.service.adapter;

import com.iflove.api.chat.domain.vo.response.ChatMemberResp;
import com.iflove.api.user.domain.entity.User;
import com.iflove.api.user.domain.enums.WSRespTypeEnum;
import com.iflove.api.user.domain.vo.response.ws.WSBaseResp;
import com.iflove.api.user.domain.vo.response.ws.WSMemberChange;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
public class MemberAdapter {

    public static WSBaseResp<WSMemberChange> buildMemberAddWS(Long roomId, User user) {
        WSBaseResp<WSMemberChange> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(WSRespTypeEnum.MEMBER_CHANGE.getType());
        wsBaseResp.setData(WSMemberChange.builder()
                .activeStatus(user.getStatus())
                .lastOptTime(user.getLastOptTime())
                .uid(user.getId())
                .roomId(roomId)
                .changeType(WSMemberChange.CHANGE_TYPE_ADD)
                .build());
        return wsBaseResp;
    }

    public static WSBaseResp<WSMemberChange> buildMemberDelWS(Long roomId, Long target) {
        WSBaseResp<WSMemberChange> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(WSRespTypeEnum.MEMBER_CHANGE.getType());
        wsBaseResp.setData(WSMemberChange.builder()
                .uid(target)
                .roomId(roomId)
                .changeType(WSMemberChange.CHANGE_TYPE_REMOVE)
                .build()
        );
        return wsBaseResp;
    }

    public static List<ChatMemberResp> buildMember(List<User> list) {
        return list.stream().map(a -> ChatMemberResp.builder()
                .activeStatus(a.getStatus())
                .lastOptTime(a.getLastOptTime())
                .uid(a.getId())
                .build())
                .collect(Collectors.toList());
    }
}
