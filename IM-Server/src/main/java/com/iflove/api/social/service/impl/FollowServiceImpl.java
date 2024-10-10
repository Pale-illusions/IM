package com.iflove.api.social.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.iflove.api.social.dao.FollowDao;
import com.iflove.api.social.domain.entity.Follow;
import com.iflove.api.social.domain.enums.SubscribeStatusEnum;
import com.iflove.api.social.domain.vo.response.FollowInfoResp;
import com.iflove.api.social.service.FollowService;
import com.iflove.api.social.service.adapter.FollowAdapter;
import com.iflove.api.user.dao.UserDao;
import com.iflove.api.user.domain.entity.User;
import com.iflove.common.annotation.RedissonLock;
import com.iflove.common.domain.vo.request.PageBaseReq;
import com.iflove.common.domain.vo.response.PageBaseResp;
import com.iflove.common.domain.vo.response.RestBean;
import com.iflove.common.exception.BusinessException;
import com.iflove.common.exception.FollowErrorEnum;
import com.iflove.common.exception.UserErrorEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Service
public class FollowServiceImpl implements FollowService {
    @Resource
    private FollowDao followDao;
    @Resource
    private UserDao userDao;

    /**
     * 关注
     * @param uid 用户id
     * @param targetId 关注对象
     * @return {@link RestBean}
     */
    @Override
    @Transactional
    @RedissonLock(key = "#uid")
    public RestBean<Void> subscribe(Long uid, Long targetId) {
        // 不能对自己操作
        if (Objects.equals(uid, targetId)) {
            throw new BusinessException(FollowErrorEnum.OPERATION_ON_SELF);
        }
        User target = userDao.getById(targetId);
        // 目标对象不存在
        if (Objects.isNull(target)) {
            throw new BusinessException(UserErrorEnum.USER_NOT_FOUND);
        }
        Follow followee = followDao.getFollowee(uid, targetId);
        // 禁止重复关注
        if (Objects.nonNull(followee)
                && Objects.equals(SubscribeStatusEnum.of(followee.getStatus()), SubscribeStatusEnum.SUBSCRIBE)) {
            throw new BusinessException(FollowErrorEnum.OPERATION_REPEAT);
        }
        // 保存关注
        followDao.saveOrUpdate(FollowAdapter.buildSubscribe(followee, uid, targetId));
        return RestBean.success();
    }

    /**
     * 取关
     * @param uid 用户id
     * @param targetId 取关对象
     * @return {@link RestBean}
     */
    @Override
    @Transactional
    @RedissonLock(key = "#uid")
    public RestBean<Void> unsubscribe(Long uid, Long targetId) {
        // 不能对自己操作
        if (Objects.equals(uid, targetId)) {
            throw new BusinessException(FollowErrorEnum.OPERATION_ON_SELF);
        }
        User target = userDao.getById(targetId);
        // 目标对象不存在
        if (Objects.isNull(target)) {
            throw new BusinessException(UserErrorEnum.USER_NOT_FOUND);
        }
        Follow followee = followDao.getFollowee(uid, targetId);
        // 禁止重复操作
        if (Objects.isNull(followee)
                || Objects.equals(SubscribeStatusEnum.of(followee.getStatus()), SubscribeStatusEnum.UNSUBSCRIBE)) {
            throw new BusinessException(FollowErrorEnum.OPERATION_REPEAT);
        }
        // 取关
        followDao.unsubscribe(followee);
        return RestBean.success();
    }

    /**
     * 粉丝列表
     * @param uid 用户id
     * @param req 分页请求
     * @return {@link RestBean}<{@link PageBaseResp}<{@link FollowInfoResp}
     */
    @Override
    public RestBean<PageBaseResp<FollowInfoResp>> followerPage(Long uid, PageBaseReq req) {
        IPage<Follow> followerPage = followDao.getFollowerPage(uid, req.plusPage());
        if (CollectionUtil.isEmpty(followerPage.getRecords())) {
            return RestBean.success(PageBaseResp.empty());
        }
        // 返回数据
        return RestBean.success(
                PageBaseResp.init(followerPage, FollowAdapter.buildFollowerInfoList(followerPage.getRecords()))
        );
    }

    /**
     * 关注列表
     * @param uid 用户id
     * @param req 分页请求
     * @return {@link RestBean}<{@link PageBaseResp}<{@link FollowInfoResp}
     */
    @Override
    public RestBean<PageBaseResp<FollowInfoResp>> followeePage(Long uid, PageBaseReq req) {
        IPage<Follow> followeePage = followDao.getFolloweePage(uid, req.plusPage());
        if (CollectionUtil.isEmpty(followeePage.getRecords())) {
            return RestBean.success(PageBaseResp.empty());
        }
        // 返回数据
        return RestBean.success(
                PageBaseResp.init(followeePage, FollowAdapter.buildFolloweeInfoList(followeePage.getRecords()))
        );
    }

    /**
     * 好友列表
     * @param uid 用户id
     * @param req 分页请求
     * @return {@link RestBean}<{@link PageBaseResp}<{@link FollowInfoResp}
     */
    @Override
    public RestBean<PageBaseResp<FollowInfoResp>> friendsPage(Long uid, PageBaseReq req) {
        IPage<FollowInfoResp> friendsPage = followDao.getFriendsPage(uid, req.plusPage());
        if (CollectionUtil.isEmpty(friendsPage.getRecords())) {
            return RestBean.success(PageBaseResp.empty());
        }
        // 返回数据
        return RestBean.success(
                PageBaseResp.init(friendsPage, friendsPage.getRecords())
        );
    }
}
