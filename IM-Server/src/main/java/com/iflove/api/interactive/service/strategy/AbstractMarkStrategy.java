package com.iflove.api.interactive.service.strategy;

import com.iflove.api.interactive.dao.LikeDao;
import com.iflove.api.interactive.domain.enums.ActionTypeEnum;
import com.iflove.api.interactive.domain.enums.MarkTypeEnum;
import com.iflove.common.event.VideoScoreComputeEvent;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
public abstract class AbstractMarkStrategy {
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @PostConstruct
    private void init() {
        MarkFactory.register(getTypeEnum().getType(), this);
    }

    protected abstract MarkTypeEnum getTypeEnum();

    @Transactional
    public void mark(Long uid, Long videoId) {
        doMark(uid, videoId);
    }

    @Transactional
    public void unMark(Long uid, Long videoId) {
        doUnMark(uid, videoId);
    }

    protected void doMark(Long uid, Long videoId) {
        exec(uid, videoId, ActionTypeEnum.MARK);
    }

    protected void doUnMark(Long uid, Long videoId) {
        exec(uid, videoId, ActionTypeEnum.UN_MARK);
    }

    protected void exec(Long uid, Long videoId, ActionTypeEnum actType) {
        MarkTypeEnum markType = this.getTypeEnum();
        boolean exist = LikeDao.exist(uid, videoId, markType);
        // 取消的类型，Redis一定有记录，没有就直接跳过操作
        // 确认的类型，如果Redis有记录，直接跳过，不进行重复操作
        if ((!exist && Objects.equals(actType, ActionTypeEnum.UN_MARK))
                || (exist && Objects.equals(actType, ActionTypeEnum.MARK))) {
            return;
        }
        LikeDao.update(uid, videoId, markType, exist);
        // 发布事件，等待计算分数
        applicationEventPublisher.publishEvent(new VideoScoreComputeEvent(this, videoId));
    }
}
