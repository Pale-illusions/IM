package com.iflove.api.chat.service.cache;

import com.iflove.api.chat.dao.MessageDao;
import com.iflove.api.chat.domain.entity.Message;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 消息相关缓存
 */
@Component
public class MsgCache {
    @Resource
    private MessageDao messageDao;

    @Cacheable(cacheNames = "msg", key = "'msg'+#msgId")
    public Message getMsg(Long msgId) {
        return messageDao.getById(msgId);
    }

    @CacheEvict(cacheNames = "msg", key = "'msg'+#msgId")
    public Message evictMsg(Long msgId) {
        return null;
    }
}
