package com.iflove.api.social.service.adapter;

import com.iflove.api.social.domain.entity.Follow;
import com.iflove.api.social.domain.enums.SubscribeStatusEnum;

import java.util.Optional;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
public class FollowAdapter {

    public static Follow buildSubscribe(Follow followee, Long uid, Long targetId) {
        return Optional.ofNullable(followee)
                .map(f -> Follow.builder()
                        .id(f.getId())
                        .followerId(uid)
                        .followeeId(targetId)
                        .status(SubscribeStatusEnum.SUBSCRIBE.getStauts())
                        .build())
                .orElseGet(() -> Follow.builder()
                        .followerId(uid)
                        .followeeId(targetId)
                        .status(SubscribeStatusEnum.SUBSCRIBE.getStauts())
                        .build());
    }
}
