package com.iflove.api.interactive.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import com.iflove.api.interactive.domain.entity.Comment;
import com.iflove.api.interactive.domain.vo.request.CommentPublishReq;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
public class CommentAdapter {

    public static Comment buildCommentSave(CommentPublishReq req, Long videoId, Long userId) {
        Comment comment = new Comment();
        BeanUtil.copyProperties(req, comment);
        comment.setVideoId(videoId);
        comment.setUserId(userId);
        return comment;
    }

}
