package com.iflove.api.interactive.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflove.api.interactive.domain.entity.Comment;
import com.iflove.api.interactive.mapper.CommentMapper;
import org.springframework.stereotype.Service;

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
}




