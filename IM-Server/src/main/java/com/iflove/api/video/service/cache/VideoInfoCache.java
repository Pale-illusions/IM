package com.iflove.api.video.service.cache;

import com.iflove.api.video.dao.TagDao;
import com.iflove.api.video.dao.VideoDao;
import com.iflove.api.video.dao.VideoTagDao;
import com.iflove.api.video.domain.dto.VideoDTO;
import com.iflove.api.video.domain.entity.Tag;
import com.iflove.api.video.domain.entity.Video;
import com.iflove.api.video.domain.entity.VideoTag;
import com.iflove.api.video.service.adapter.VideoAdapter;
import com.iflove.common.constant.RedisKey;
import com.iflove.common.service.cache.AbstractRedisStringCache;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Component
public class VideoInfoCache extends AbstractRedisStringCache<Long, VideoDTO> {
    @Resource
    private VideoDao videoDao;
    @Resource
    private TagDao tagDao;
    @Resource
    private VideoTagDao videoTagDao;

    @Override
    protected String getKey(Long req) {
        return RedisKey.getKey(RedisKey.VIDEO_INFO_STRING, req);
    }

    @Override
    protected Long getExpireSeconds() {
        return 60 * 5L;
    }

    @Override
    protected Map<Long, VideoDTO> load(List<Long> req) {
        // 获取视频
        List<Video> videos = videoDao.listByIds(req);
        // 获取视频标签，映射为 视频id -> 标签 Map
        Map<Long, List<String>> videoIdTagMap = videos.stream().collect(Collectors.toMap(Video::getId, video -> {
            List<VideoTag> videoTags = videoTagDao.getByVideoId(video.getId());
            List<Tag> tags = tagDao.listByIds(videoTags.stream().map(VideoTag::getTagId).collect(Collectors.toList()));
            return tags.stream().map(Tag::getTagName).collect(Collectors.toList());
        }));
        // 组装数据
        return videos.stream()
                .collect(Collectors.toMap(Video::getId, video -> VideoAdapter.buildVideoDTO(video, videoIdTagMap)));
    }
}
