package com.iflove.api.interactive.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.iflove.api.interactive.dao.CommentDao;
import com.iflove.api.interactive.domain.entity.Comment;
import com.iflove.api.interactive.domain.enums.CommentTypeEnum;
import com.iflove.api.interactive.domain.vo.request.CommentDeleteReq;
import com.iflove.api.interactive.domain.vo.request.CommentPageReq;
import com.iflove.api.interactive.domain.vo.request.CommentPublishReq;
import com.iflove.api.interactive.service.CommentService;
import com.iflove.api.interactive.service.adapter.CommentAdapter;
import com.iflove.api.interactive.service.cache.CommentCache;
import com.iflove.api.video.dao.VideoDao;
import com.iflove.api.video.domain.dto.VideoDTO;
import com.iflove.api.video.service.cache.VideoInfoCache;
import com.iflove.common.constant.RedisKey;
import com.iflove.common.domain.vo.response.PageBaseResp;
import com.iflove.common.domain.vo.response.RestBean;
import com.iflove.common.event.VideoScoreComputeEvent;
import com.iflove.common.exception.BusinessException;
import com.iflove.common.exception.CommentErrorEnum;
import com.iflove.sensitive.service.SensitiveWordBs;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utils.RedisUtil;

import java.util.List;
import java.util.Objects;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Resource
    private CommentDao commentDao;
    @Resource
    private CommentCache commentCache;
    @Resource
    private VideoInfoCache videoInfoCache;
    @Resource
    private SensitiveWordBs sensitiveWordBs;
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * 评论
     * @param uid 用户id
     * @param req 评论请求
     * @return {@link RestBean}
     */
    @Override
    @Transactional
    public RestBean<Void> publish(Long uid, CommentPublishReq req) {
        // 过滤敏感词
        req.setContent(sensitiveWordBs.filter(req.getContent()));
        // 获取评论对象类型
        CommentTypeEnum commentTypeEnum = CommentTypeEnum.of(req.getType());
        // 视频id
        Long videoId;
        // 对视频评论
        if (Objects.equals(commentTypeEnum, CommentTypeEnum.VIDEO)) {
            VideoDTO target = videoInfoCache.get(req.getTargetId());
            if (Objects.isNull(target)) {
                throw new BusinessException(CommentErrorEnum.COMMENT_TARGET_NOT_FOUND);
            }
            videoId = target.getId();
            commentDao.save(CommentAdapter.buildCommentSave(req, videoId, uid));
        }
        // 对评论评论
        else if (Objects.equals(commentTypeEnum, CommentTypeEnum.COMMENT)) {
            Comment target = commentCache.getComment(req.getTargetId());
            if (Objects.isNull(target)) {
                throw new BusinessException(CommentErrorEnum.COMMENT_TARGET_NOT_FOUND);
            }
            videoId = target.getVideoId();
            commentDao.save(CommentAdapter.buildCommentSave(req, videoId, uid));
        }
        // 不支持的评论对象
        else {
            throw new BusinessException(CommentErrorEnum.TYPE_NOT_SUPPORTED);
        }
        // 发布视频分数计算事件
        applicationEventPublisher.publishEvent(new VideoScoreComputeEvent(this, videoId));
        return RestBean.success();
    }

    /**
     * 评论列表
     * @param req 评论列表请求
     * @return {@link RestBean}<{@link PageBaseResp}<{@link Comment}
     */
    @Override
    public RestBean<PageBaseResp<Comment>> listComment(CommentPageReq req) {
        // 不支持的评论类型
        if (Objects.isNull(CommentTypeEnum.of(req.getType()))) {
            throw new BusinessException(CommentErrorEnum.TYPE_NOT_SUPPORTED);
        }
        IPage<Comment> commentPage = commentDao.listComment(req);
        if (CollectionUtil.isEmpty(commentPage.getRecords())) {
            return RestBean.success(PageBaseResp.empty());
        }
        // 返回数据
        return RestBean.success(
               PageBaseResp.init(commentPage)
        );
    }

    /**
     * 删除评论
     * @param req 评论删除请求
     * @return {@link RestBean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestBean<Void> deleteComment(CommentDeleteReq req) {
        // 获取评论对象类型
        CommentTypeEnum commentTypeEnum = CommentTypeEnum.of(req.getType());
        // 视频id
        Long videoId;
        // 视频评论删除
        if (Objects.equals(commentTypeEnum, CommentTypeEnum.VIDEO)) {
            VideoDTO target = videoInfoCache.get(req.getTargetId());
            if (Objects.isNull(target)) {
                throw new BusinessException(CommentErrorEnum.COMMENT_TARGET_NOT_FOUND);
            }
            videoId = target.getId();
            commentDao.deleteVideoComment(videoId);
        }
        // 评论的评论删除
        else if (Objects.equals(commentTypeEnum, CommentTypeEnum.COMMENT)) {
            Comment target = commentCache.getComment(req.getTargetId());
            if (Objects.isNull(target)) {
                throw new BusinessException(CommentErrorEnum.COMMENT_TARGET_NOT_FOUND);
            }
            videoId = target.getVideoId();
            dfsDelete(target.getId());
        }
        // 不支持的评论对象
        else {
            throw new BusinessException(CommentErrorEnum.TYPE_NOT_SUPPORTED);
        }
        // 发布视频分数计算事件
        applicationEventPublisher.publishEvent(new VideoScoreComputeEvent(this, videoId));
        return RestBean.success();
    }

    /**
     * dfs删除评论
     * @param targetId 目标id
     */
    private void dfsDelete(Long targetId) {
        List<Comment> childs = commentDao.getChilds(targetId);
        commentDao.deleteComment(targetId);
        commentCache.evictComment(targetId);
        if (CollectionUtil.isEmpty(childs)) return;
        childs.forEach(child -> dfsDelete(child.getId()));
    }

}
