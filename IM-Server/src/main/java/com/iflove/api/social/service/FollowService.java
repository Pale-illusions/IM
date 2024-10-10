package com.iflove.api.social.service;

import com.iflove.api.social.domain.vo.response.FollowInfoResp;
import com.iflove.common.domain.vo.request.PageBaseReq;
import com.iflove.common.domain.vo.response.PageBaseResp;
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

    /**
     * 粉丝列表
     * @param uid 用户id
     * @param req 分页请求
     * @return {@link RestBean}<{@link PageBaseResp}<{@link FollowInfoResp}
     */
    RestBean<PageBaseResp<FollowInfoResp>> followerPage(Long uid, PageBaseReq req);

    /**
     * 关注列表
     * @param uid 用户id
     * @param req 分页请求
     * @return {@link RestBean}<{@link PageBaseResp}<{@link FollowInfoResp}
     */
    RestBean<PageBaseResp<FollowInfoResp>> followeePage(Long uid, PageBaseReq req);

    /**
     * 好友列表
     * @param uid 用户id
     * @param req 分页请求
     * @return {@link RestBean}<{@link PageBaseResp}<{@link FollowInfoResp}
     */
    RestBean<PageBaseResp<FollowInfoResp>> friendsPage(Long uid, PageBaseReq req);
}
