package com.iflove.api.social.service.adapter;

import com.iflove.api.social.domain.entity.Follow;
import com.iflove.api.social.domain.enums.SubscribeStatusEnum;
import com.iflove.api.social.domain.vo.response.FollowInfoResp;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    /**
     * 关注对象信息列表
     * @param records
     * @return
     */
    public static List<FollowInfoResp> buildFolloweeInfoList(List<Follow> records) {
        return records
                .stream()
                .map(o -> FollowInfoResp.builder()
                        .id(o.getFolloweeId())
                        .followDate(o.getUpdateTime())
                        .build()
                )
                .collect(Collectors.toList());
    }

    /**
     * 粉丝信息列表
     * @param records
     * @return
     */
    public static List<FollowInfoResp> buildFollowerInfoList(List<Follow> records) {
        return records
                .stream()
                .map(o -> FollowInfoResp.builder()
                        .id(o.getFollowerId())
                        .followDate(o.getUpdateTime())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
