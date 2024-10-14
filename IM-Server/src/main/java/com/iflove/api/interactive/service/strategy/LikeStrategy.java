package com.iflove.api.interactive.service.strategy;

import com.iflove.api.interactive.domain.enums.MarkTypeEnum;
import org.springframework.stereotype.Component;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Component
public class LikeStrategy extends AbstractMarkStrategy {
    @Override
    protected MarkTypeEnum getTypeEnum() {
        return MarkTypeEnum.LIKE;
    }

    @Override
    protected void doMark(Long uid, Long videoId) {
        super.doMark(uid, videoId);
        //同时取消点踩的动作
        MarkFactory.getStrategyNoNull(MarkTypeEnum.DISLIKE.getType()).unMark(uid, videoId);
    }
}
