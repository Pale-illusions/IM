package com.iflove.api.interactive.service.strategy;

import com.iflove.api.interactive.domain.enums.MarkTypeEnum;
import org.springframework.stereotype.Component;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Component
public class DisLikeStrategy extends AbstractMarkStrategy {
    @Override
    protected MarkTypeEnum getTypeEnum() {
        return MarkTypeEnum.DISLIKE;
    }

    @Override
    protected void doMark(Long uid, Long videoId) {
        super.doMark(uid, videoId);
        //同时取消点赞的动作
        MarkFactory.getStrategyNoNull(MarkTypeEnum.LIKE.getType()).unMark(uid, videoId);
    }
}
