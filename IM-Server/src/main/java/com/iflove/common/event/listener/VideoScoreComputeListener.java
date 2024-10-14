package com.iflove.common.event.listener;

import com.iflove.common.constant.RedisKey;
import com.iflove.common.event.UserOnlineEvent;
import com.iflove.common.event.VideoScoreComputeEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import utils.RedisUtil;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Component
public class VideoScoreComputeListener {
    /**
     * 分数相关数据变更，存入Redis，等待计算分数
     * @param event 视频分数计算事件
     */
    @Async
    @EventListener(classes = VideoScoreComputeEvent.class)
    public void compute(VideoScoreComputeEvent event) {
        RedisUtil.sSet(RedisKey.getKey(RedisKey.VIDEO_SCORE_COMPUTEWAIT), event.getVideoId());
    }
}
