package com.iflove.api.video.service;

import com.iflove.api.video.domain.vo.request.PublishReq;
import com.iflove.api.video.domain.vo.response.VideoInfoResp;
import com.iflove.common.domain.vo.response.RestBean;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
public interface VideoService {
    /**
     * 发布视频
     * @param uid 用户id
     * @param req 发布视频请求
     * @return {@link RestBean}
     */
    RestBean<Void> publish(Long uid, PublishReq req);

    /**
     * 浏览视频
     * @param videoId 视频id
     * @return {@link RestBean}<{@link VideoInfoResp}
     */
    RestBean<VideoInfoResp> browse(Long videoId);
}
