package com.iflove.api.video.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import com.iflove.api.video.domain.dto.VideoDTO;
import com.iflove.api.video.domain.entity.Tag;
import com.iflove.api.video.domain.entity.Video;
import com.iflove.api.video.domain.vo.request.PublishReq;
import com.iflove.api.video.domain.vo.response.VideoInfoResp;

import java.util.List;
import java.util.Map;
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
}
