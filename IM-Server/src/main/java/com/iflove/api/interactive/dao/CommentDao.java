package com.iflove.api.interactive.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflove.api.interactive.domain.entity.Comment;
import com.iflove.api.interactive.domain.enums.CommentTypeEnum;
import com.iflove.api.interactive.domain.vo.request.CommentPageReq;
import com.iflove.api.interactive.mapper.CommentMapper;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
* @author IFLOVE
* @description 针对表【comment(评论表)】的数据库操作Service实现
* @createDate 2024-10-10 19:47:04
*/
@Service
public class CommentDao extends ServiceImpl<CommentMapper, Comment> {

    public Long getVideoCommentCount(String videoId) {
        return lambdaQuery()
                .eq(Comment::getVideoId, videoId)
                .count();
    }

    public IPage<Comment> listComment(CommentPageReq req) {
        return lambdaQuery()
                .eq(Comment::getType, req.getType())
                .eq(Comment::getTargetId, req.getTargetId())
                .page(req.plusPage());
    }
}




