package com.iflove.api.interactive.service.impl;

import cn.hutool.core.lang.Pair;
import com.iflove.api.interactive.dao.LikeDao;
import com.iflove.api.interactive.domain.enums.ActionTypeEnum;
import com.iflove.api.interactive.domain.enums.MarkTypeEnum;
import com.iflove.api.interactive.domain.vo.request.VideoActionReq;
import com.iflove.api.interactive.domain.vo.response.ActionListResp;
import com.iflove.api.interactive.service.LikeService;
import com.iflove.api.interactive.service.adapter.LikeAdapter;
import com.iflove.api.interactive.service.strategy.AbstractMarkStrategy;
import com.iflove.api.interactive.service.strategy.MarkFactory;
import com.iflove.api.video.dao.VideoDao;
import com.iflove.api.video.domain.dto.VideoDTO;
import com.iflove.api.video.service.cache.VideoInfoCache;
import com.iflove.common.annotation.RedissonLock;
import com.iflove.common.constant.RedisKey;
import com.iflove.common.domain.vo.request.CursorPageBaseReq;
import com.iflove.common.domain.vo.response.CursorPageBaseResp;
import com.iflove.common.domain.vo.response.PageBaseResp;
import com.iflove.common.domain.vo.response.RestBean;
import com.iflove.common.exception.BusinessException;
import com.iflove.common.exception.VideoErrorEnum;
import com.iflove.common.utils.CursorUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Service
public class LikeServiceImpl implements LikeService {
    @Resource
    private VideoInfoCache videoInfoCache;

    /**
     * 点赞/点踩
     * @param uid 用户id
     * @param req 视频点赞点踩请求
     * @return {@link RestBean}
     */
    @Override
    @RedissonLock(key = "#uid")
    public RestBean<Void> mark(Long uid, VideoActionReq req) {
        VideoDTO dto = videoInfoCache.get(req.getVideoId());
        if (Objects.isNull(dto)) {
            throw new BusinessException(VideoErrorEnum.VIDEO_NOT_EXIST);
        }
        AbstractMarkStrategy strategy = MarkFactory.getStrategyNoNull(req.getMarkType());
        switch (ActionTypeEnum.of(req.getActionType())) {
            case MARK -> strategy.mark(uid, req.getVideoId());
            case UN_MARK -> strategy.unMark(uid, req.getVideoId());
        }
        return RestBean.success();
    }

    /**
     * 点赞列表
     * @param uid 用户id
     * @param req 游标分页请求
     * @return {@link RestBean}<{@link PageBaseResp}<{@link ActionListResp}
     */
    @Override
    public RestBean<CursorPageBaseResp<ActionListResp>> likeList(Long uid, CursorPageBaseReq req) {
        return actionList(req, RedisKey.getKey(RedisKey.VIDEO_LIKE_RELATION, uid));
    }

    /**
     * 点踩列表
     * @param uid 用户id
     * @param req 游标分页请求
     * @return {@link RestBean}<{@link PageBaseResp}<{@link ActionListResp}
     */
    @Override
    public RestBean<CursorPageBaseResp<ActionListResp>> unlikeList(Long uid, CursorPageBaseReq req) {
        return actionList(req, RedisKey.getKey(RedisKey.VIDEO_DISLIKE_RELATION, uid));
    }

    private RestBean<CursorPageBaseResp<ActionListResp>> actionList(CursorPageBaseReq req, String key) {
        CursorPageBaseResp<Pair<Long, Double>> page = CursorUtils.getCursorPageByRedis(req, key, Long::parseLong);
        CursorPageBaseResp<ActionListResp> result = LikeAdapter.buildPageResp(page);
        return RestBean.success(result);
    }
}
