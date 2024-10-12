package com.iflove.api.video.service.impl;

import com.iflove.api.video.dao.VideoDao;
import com.iflove.api.video.domain.entity.Video;
import com.iflove.api.video.service.RankService;
import com.iflove.api.video.service.cache.VideoInfoCache;
import com.iflove.common.constant.RedisKey;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import utils.RedisUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Service
@Slf4j
public class RankServiceImpl implements RankService {
    @Resource
    private VideoInfoCache videoInfoCache;
    @Resource
    private VideoDao videoDao;

    // 网站纪元
    private static final Date epoch;

    // 初始化网站纪元，用我的生日捏
    static {
        try {
            epoch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2005-01-19 00:00:00");
        } catch (ParseException e) {
            throw new RuntimeException("初始化网站纪元失败!", e);
        }
    }

    /**
     * 计算视频分数
     */
    @Override
    // 每1分钟执行一次，测试用
    @Scheduled(cron = "0 * * * * ?")
    // 每2小时执行一次，正式上线用
//    @Scheduled(cron = "0 0 */2 * * ?")
    public void compute() {
        Set<String> videoIds = RedisUtil.sGet(RedisKey.getKey(RedisKey.VIDEO_SCORE_COMPUTEWAIT));
        if (Objects.isNull(videoIds) || videoIds.isEmpty()) {
            log.info("[任务取消] 没有要计算的视频!");
            return;
        }
        log.info("[任务开始] 正在刷新视频分数, 共计{}个视频", videoIds.size());
        for (String videoId : videoIds) {
            refresh(videoId);
            RedisUtil.setRemove(RedisKey.getKey(RedisKey.VIDEO_SCORE_COMPUTEWAIT), videoId);
        }
        log.info("[任务结束] 视频分数刷新完毕!");
    }

    /**
     * 刷新视频分数
     * 计算公式：
     * Score = log(评论数 * 10 + 点赞数 * 5 - 点踩数 * 3 + 点击量 * 1) + (发布时间 - 网站纪元) / (3600 * 60 * 24)
     */
    private void refresh(String videoId) {
        long id = Long.parseLong(videoId);
        Video video = videoDao.getById(id);
        if (Objects.isNull(video)) {
            log.warn("视频不存在, id={}", id);
        }
        // TODO 点赞数
        double likeCount = 0;
        // TODO 点踩数
        double disLikeCount = 0;
        // TODO 评论数
        double commentCount = 0;
        // 点击量
        double clickCount = RedisUtil.zScore(RedisKey.getKey(RedisKey.VIDEO_CLICK_COUNT), videoId);
        // 计算权重
        double w = commentCount * 10 + likeCount * 5 - disLikeCount * 3 + clickCount;
        // 分数 = 权重 + 距离天数
        // w可能小于1，因为log存在，所以送入log的最小值应该为 1
        // getTime()单位为ms
        double score = Math.log10(Math.max(1, w)) +
                (double) (video.getCreateTime().getTime() - epoch.getTime()) / (3600 * 60 * 24);
        // 更新视频分数
        video.setScore(score);
        videoDao.updateById(video);
        // 删除视频缓存
        videoInfoCache.delete(id);
        // TODO 更新 es

    }
}
