package com.iflove.api.user.service.adapter;

import com.iflove.api.user.domain.entity.UserApply;
import com.iflove.api.user.domain.enums.ApplyStatusEnum;
import com.iflove.api.user.domain.enums.ApplyTypeEnum;
import com.iflove.api.user.domain.vo.request.friend.FriendApplyReq;

import java.util.Date;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
public class FriendAdapter {

    public static UserApply buildFriendApply(Long uid, FriendApplyReq request) {
        return UserApply.builder()
                .userId(uid)
                .msg(request.getMsg())
                .status(ApplyStatusEnum.WAIT_APPROVAL.getCode())
                .type(ApplyTypeEnum.ADD_FRIEND.getCode())
                .targetId(request.getTargetUid())
                .build();
    }
}
