package com.iflove.api.social.service;

import com.iflove.common.domain.vo.response.RestBean;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
public interface FollowService {
    /**
     * 关注/取关
     * @param uid 用户id
     * @param targetId 关注对象
     * @return {@link RestBean}
     */
    RestBean<Void> subscribe(Long uid, Long targetId);

    /**
     * 取关
     * @param uid 用户id
     * @param targetId 取关对象
     * @return {@link RestBean}
     */
    RestBean<Void> unsubscribe(Long uid, Long targetId);
}
