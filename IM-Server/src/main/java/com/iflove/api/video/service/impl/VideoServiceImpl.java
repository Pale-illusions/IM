package com.iflove.api.video.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.iflove.api.video.dao.TagDao;
import com.iflove.api.video.dao.VideoDao;
import com.iflove.api.video.dao.VideoTagDao;
import com.iflove.api.video.domain.dto.VideoDTO;
import com.iflove.api.video.domain.entity.Tag;
import com.iflove.api.video.domain.entity.Video;
import com.iflove.api.video.domain.entity.VideoTag;
import com.iflove.api.video.domain.vo.request.VideoPublishReq;
import com.iflove.api.video.domain.vo.request.VideoSearchReq;
import com.iflove.api.video.domain.vo.response.VideoInfoResp;
import com.iflove.api.video.domain.vo.response.VideoSearchResp;
import com.iflove.api.video.service.ElasticSearchService;
import com.iflove.api.video.service.VideoService;
import com.iflove.api.video.service.adapter.VideoAdapter;
import com.iflove.api.video.service.cache.VideoInfoCache;
import com.iflove.common.constant.RedisKey;
import com.iflove.common.domain.vo.request.PageBaseReq;
import com.iflove.common.domain.vo.response.PageBaseResp;
import com.iflove.common.domain.vo.response.RestBean;
import com.iflove.common.exception.BusinessException;
import com.iflove.common.exception.VideoErrorEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utils.RedisUtil;

import java.util.*;

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
    private ElasticSearchService elasticSearchService;
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
    public RestBean<Void> publish(Long uid, VideoPublishReq req) {
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
        // 组装数据，保存到es
        elasticSearchService.saveVideo(VideoAdapter.buildVideoDTO(video, Map.of(video.getId(), tags)));
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
        // TODO 添加点赞量，评论量 。。。
        return RestBean.success(VideoAdapter.buildVideoInfoResp(dto));
    }

    /**
     * 搜索视频
     * @param req 视频搜索请求
     * @return {@link RestBean}<{@link PageBaseResp}<{@link VideoInfoResp}
     */
    @Override
    public RestBean<PageBaseResp<VideoSearchResp>> search(VideoSearchReq req) {
        // TODO 时间范围
        List<VideoDTO> videoDTOS = elasticSearchService.searchByTitleAndTags(req.getKeyword(), req.getTags());
        if (CollectionUtil.isEmpty(videoDTOS)) {
            return RestBean.success(PageBaseResp.empty());
        }

        // pageNo 项目设定默认(跟mybatisplus一致)从 1 开始, 但是搜索默认从 0 开始, 故-1
        int fromIndex = (req.getPageNo() - 1) * req.getPageSize();
        int toIndex = Math.min(fromIndex + req.getPageSize(), videoDTOS.size()); // 防止越界

        if (fromIndex >= videoDTOS.size()) {
            // 页数超出范围，返回空结果
            return RestBean.success(PageBaseResp.empty());
        }
        // 组装结果返回
        List<VideoDTO> result = videoDTOS.subList(fromIndex, toIndex);
        boolean isLast = toIndex == videoDTOS.size(); // 判断是否为最后一页
        return RestBean.success(
                PageBaseResp.init(req.getPageNo(), req.getPageSize(), (long) videoDTOS.size(), isLast, VideoAdapter.buildVideoSearchRespByDTO(result))
        );
    }

    /**
     * 热门排行榜
     * @param req 基础分页请求
     * @return {@link RestBean}<{@link PageBaseResp}<{@link VideoSearchResp}
     */
    @Override
    public RestBean<PageBaseResp<VideoSearchResp>> rank(PageBaseReq req) {
        IPage<Video> rankPage = videoDao.rank(req.plusPage());
        if (CollectionUtil.isEmpty(rankPage.getRecords())) {
            return RestBean.success(PageBaseResp.empty());
        }
        // 返回数据
        return RestBean.success(
                PageBaseResp.init(rankPage, VideoAdapter.buildVideoSearchRespByVideo(rankPage.getRecords()))
        );
    }

}
