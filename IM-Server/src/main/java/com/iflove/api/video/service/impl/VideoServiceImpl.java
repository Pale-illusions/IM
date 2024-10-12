package com.iflove.api.video.service.impl;

import com.iflove.api.video.dao.TagDao;
import com.iflove.api.video.dao.VideoDao;
import com.iflove.api.video.dao.VideoTagDao;
import com.iflove.api.video.domain.dto.VideoDTO;
import com.iflove.api.video.domain.entity.Tag;
import com.iflove.api.video.domain.entity.Video;
import com.iflove.api.video.domain.entity.VideoTag;
import com.iflove.api.video.domain.vo.request.PublishReq;
import com.iflove.api.video.domain.vo.response.VideoInfoResp;
import com.iflove.api.video.service.VideoService;
import com.iflove.api.video.service.adapter.VideoAdapter;
import com.iflove.api.video.service.cache.VideoInfoCache;
import com.iflove.common.constant.RedisKey;
import com.iflove.common.domain.vo.response.RestBean;
import com.iflove.common.exception.BusinessException;
import com.iflove.common.exception.VideoErrorEnum;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utils.RedisUtil;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Service
public class VideoServiceImpl implements VideoService {
    @Resource
    private VideoDao videoDao;
    @Resource
    private TagDao tagDao;
    @Resource
    private VideoTagDao videoTagDao;
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;
    @Resource
    private VideoInfoCache videoInfoCache;

    /**
     * 发布视频
     * @param uid 用户id
     * @param req 发布视频请求
     * @return {@link RestBean}
     */
    @Override
    @Transactional
    public RestBean<Void> publish(Long uid, PublishReq req) {
        Video video = VideoAdapter.buildVideoPost(uid, req);
        // 保存视频
        videoDao.save(video);
        // 保存标签
        List<String> tags = req.getTags();
        tags.forEach(tagName -> {
            Tag tag = Optional.ofNullable(tagDao.getByTagName(tagName))
                    .orElseGet(() -> {
                        Tag newTag = Tag.init(tagName);
                        tagDao.save(newTag);
                        return newTag;
                    });
            videoTagDao.save(VideoTag.init(video.getId(), tag.getId()));
        });
        // TODO es保存 发布事件

        return RestBean.success();
    }

    /**
     * 浏览视频
     * @param videoId 视频id
     * @return {@link RestBean}<{@link VideoInfoResp}
     */
    @Override
    public RestBean<VideoInfoResp> browse(Long videoId) {
        VideoDTO dto = videoInfoCache.get(videoId);
        // 视频不存在
        if (Objects.isNull(dto)) {
            throw new BusinessException(VideoErrorEnum.VIDEO_NOT_EXIST);
        }
        // 增加视频点击量
        RedisUtil.zIncrementScore(RedisKey.getKey(RedisKey.VIDEO_CLICK_COUNT), videoId.toString(), 1);
        // 分数相关数据变更，存入Redis，等待计算分数
        RedisUtil.sSet(RedisKey.getKey(RedisKey.VIDEO_SCORE_COMPUTEWAIT), videoId);
        // 组装数据
        return RestBean.success(VideoAdapter.buildVideoInfoResp(dto));
    }


}
