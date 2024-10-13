package com.iflove.api.interactive.service.cache;

import com.iflove.api.chat.domain.entity.Message;
import com.iflove.api.interactive.dao.CommentDao;
import com.iflove.api.interactive.domain.entity.Comment;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Component
public class CommentCache {
    @Resource
    private CommentDao commentDao;

    @Cacheable(cacheNames = "comment", key = "'comment'+#commentId")
    public Comment getComment(Long commentId) {
        return commentDao.getById(commentId);
    }

    @CacheEvict(cacheNames = "comment", key = "'comment'+#commentId")
    public Message evictComment(Long commentId) {
        return null;
    }
}
