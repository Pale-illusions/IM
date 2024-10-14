package com.iflove.api.interactive.service.impl;

import com.iflove.api.interactive.dao.LikeDao;
import com.iflove.api.interactive.domain.enums.ActionTypeEnum;
import com.iflove.api.interactive.domain.enums.MarkTypeEnum;
import com.iflove.api.interactive.domain.vo.request.VideoActionReq;
import com.iflove.api.interactive.domain.vo.response.ActionListResp;
import com.iflove.api.interactive.service.LikeService;
import com.iflove.api.interactive.service.strategy.AbstractMarkStrategy;
import com.iflove.api.interactive.service.strategy.MarkFactory;
import com.iflove.common.annotation.RedissonLock;
import com.iflove.common.domain.vo.response.PageBaseResp;
import com.iflove.common.domain.vo.response.RestBean;
import org.springframework.stereotype.Service;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Service
public class LikeServiceImpl implements LikeService {

    /**
     * 点赞/点踩
     * @param uid 用户id
     * @param req 视频点赞点踩请求
     * @return {@link RestBean}
     */
    @Override
    @RedissonLock(key = "#uid")
    public RestBean<Void> mark(Long uid, VideoActionReq req) {
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
     * @return {@link RestBean}<{@link PageBaseResp}<{@link ActionListResp}
     */
    @Override
    public RestBean<PageBaseResp<ActionListResp>> likeList(Long uid) {
//        LikeDao.likeList(uid)
        return null;
    }

    /**
     * 点踩列表
     * @param uid 用户id
     * @return {@link RestBean}<{@link PageBaseResp}<{@link ActionListResp}
     */
    @Override
    public RestBean<PageBaseResp<ActionListResp>> unlikeList(Long uid) {
        return null;
    }
}
