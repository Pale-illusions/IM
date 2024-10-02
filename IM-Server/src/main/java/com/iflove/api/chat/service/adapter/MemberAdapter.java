package com.iflove.api.chat.service.adapter;

import com.iflove.api.user.domain.entity.User;
import com.iflove.api.user.domain.enums.WSRespTypeEnum;
import com.iflove.api.user.domain.vo.response.ws.WSBaseResp;
import com.iflove.api.user.domain.vo.response.ws.WSMemberChange;
import org.springframework.stereotype.Component;

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
}
