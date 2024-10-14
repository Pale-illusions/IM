package com.iflove.api.interactive.service;

import com.iflove.api.interactive.domain.vo.request.VideoActionReq;
import com.iflove.api.interactive.domain.vo.response.ActionListResp;
import com.iflove.common.domain.vo.response.PageBaseResp;
import com.iflove.common.domain.vo.response.RestBean;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
public interface LikeService {
    /**
     * 点赞/点踩
     * @param uid 用户id
     * @param req 视频点赞点踩请求
     * @return {@link RestBean}
     */
    RestBean<Void> mark(Long uid, VideoActionReq req);

    /**
     * 点赞列表
     * @param uid 用户id
     * @return {@link RestBean}<{@link PageBaseResp}<{@link ActionListResp}
     */
    RestBean<PageBaseResp<ActionListResp>> likeList(Long uid);

    /**
     * 点踩列表
     * @param uid 用户id
     * @return {@link RestBean}<{@link PageBaseResp}<{@link ActionListResp}
     */
    RestBean<PageBaseResp<ActionListResp>> unlikeList(Long uid);
}
