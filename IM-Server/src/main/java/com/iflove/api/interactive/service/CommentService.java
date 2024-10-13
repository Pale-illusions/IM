package com.iflove.api.interactive.service;

import com.iflove.api.interactive.domain.vo.request.CommentPublishReq;
import com.iflove.common.domain.vo.response.RestBean;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
public interface CommentService {

    /**
     * 评论
     * @param uid 用户id
     * @param req 评论请求
     * @return {@link RestBean}
     */
    RestBean<Void> publish(Long uid, CommentPublishReq req);
}
