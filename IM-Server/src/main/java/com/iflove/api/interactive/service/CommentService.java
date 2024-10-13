package com.iflove.api.interactive.service;

import com.iflove.api.interactive.domain.entity.Comment;
import com.iflove.api.interactive.domain.vo.request.CommentDeleteReq;
import com.iflove.api.interactive.domain.vo.request.CommentPageReq;
import com.iflove.api.interactive.domain.vo.request.CommentPublishReq;
import com.iflove.common.domain.vo.response.PageBaseResp;
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

    /**
     * 评论列表
     * @param req 评论列表请求
     * @return {@link RestBean}<{@link PageBaseResp}<{@link Comment}
     */
    RestBean<PageBaseResp<Comment>> listComment(CommentPageReq req);

    /**
     * 删除评论
     * @param req 评论删除请求
     * @return {@link RestBean}
     */
    RestBean<Void> deleteComment(CommentDeleteReq req);
}
