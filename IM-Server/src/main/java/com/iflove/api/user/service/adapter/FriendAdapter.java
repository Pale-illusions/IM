package com.iflove.api.user.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import com.iflove.api.user.domain.entity.User;
import com.iflove.api.user.domain.entity.UserApply;
import com.iflove.api.user.domain.entity.UserFriend;
import com.iflove.api.user.domain.enums.ApplyStatusEnum;
import com.iflove.api.user.domain.enums.ApplyTypeEnum;
import com.iflove.api.user.domain.vo.request.friend.FriendApplyReq;
import com.iflove.api.user.domain.vo.response.friend.FriendApplyResp;
import com.iflove.api.user.domain.vo.response.friend.FriendInfoResp;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public static List<FriendApplyResp> buildFriendApplyList(List<UserApply> records) {
        return records
                .stream()
                .map(o -> FriendApplyResp
                        .builder()
                        .applyId(o.getId())
                        .uid(o.getUserId())
                        .type(o.getType())
                        .msg(o.getMsg())
                        .status(o.getStatus())
                        .build()
                )
                .collect(Collectors.toList());
    }

    public static List<FriendInfoResp> buildFriendList(List<UserFriend> list, List<User> userList) {
        Map<Long, User> userMap = userList.stream().collect(Collectors.toMap(User::getId, Function.identity()));
        return list
                .stream()
                .map(userFriend -> FriendInfoResp
                            .builder()
                            .uid(userFriend.getFriendId())
                            .activeStatus(userMap.get(userFriend.getFriendId()).getStatus())
                            .build()
                )
                .collect(Collectors.toList());
    }
}
