package com.iflove.api.video.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import com.iflove.api.video.domain.dto.VideoDTO;
import com.iflove.api.video.domain.entity.Video;
import com.iflove.api.video.domain.vo.request.PublishReq;
import com.iflove.api.video.domain.vo.response.VideoInfoResp;
import com.iflove.api.video.domain.vo.response.VideoSearchResp;
import com.iflove.common.constant.RedisKey;
import utils.RedisUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
public class VideoAdapter {
    public static Video buildVideoPost(Long uid, PublishReq req) {
        return Video.builder()
                .userId(uid)
                .url(req.getUrl())
                .title(req.getTitle())
                .description(req.getDescription())
                .build();
    }

    public static VideoInfoResp buildVideoInfoResp(VideoDTO dto) {
        VideoInfoResp resp = new VideoInfoResp();
        BeanUtil.copyProperties(dto, resp);
        return resp;
    }

    public static VideoDTO buildVideoDTO(Video video, Map<Long, List<String>> map) {
        VideoDTO resp = new VideoDTO();
        BeanUtil.copyProperties(video, resp);
        resp.setTags(map.get(video.getId()));
        return resp;
    }

    public static List<VideoSearchResp> buildVideoSearchRespByDTO(List<VideoDTO> videoDTOS) {
        return videoDTOS.stream()
                .map(v -> {
                    VideoSearchResp resp = new VideoSearchResp();
                    BeanUtil.copyProperties(v, resp);
                    resp.setClickCount(Optional
                            .ofNullable(RedisUtil.zScore(RedisKey.getKey(RedisKey.VIDEO_CLICK_COUNT), v.getId().toString()))
                            .orElse(0.0)
                            .longValue()
                    );
                    return resp;
                })
                .collect(Collectors.toList());
    }

    public static List<VideoSearchResp> buildVideoSearchRespByVideo(List<Video> videos) {
        return videos.stream()
                .map(v -> {
                    VideoSearchResp resp = new VideoSearchResp();
                    BeanUtil.copyProperties(v, resp);
                    resp.setClickCount(Optional
                            .ofNullable(RedisUtil.zScore(RedisKey.getKey(RedisKey.VIDEO_CLICK_COUNT), v.getId().toString()))
                            .orElse(0.0)
                            .longValue()
                    );
                    return resp;
                })
                .collect(Collectors.toList());
    }

}
