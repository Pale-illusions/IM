package com.iflove.api.video.service;

import com.iflove.api.video.domain.vo.request.PublishReq;
import com.iflove.api.video.domain.vo.request.VideoSearchReq;
import com.iflove.api.video.domain.vo.response.VideoInfoResp;
import com.iflove.api.video.domain.vo.response.VideoSearchResp;
import com.iflove.common.domain.vo.response.PageBaseResp;
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

    /**
     * 搜索视频
     * @param req 视频搜索请求
     * @return {@link RestBean}<{@link PageBaseResp}<{@link VideoInfoResp}
     */
    RestBean<PageBaseResp<VideoSearchResp>> search(VideoSearchReq req);
}
