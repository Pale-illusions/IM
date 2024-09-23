package com.iflove.api.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.iflove.api.user.dao.UserApplyDao;
import com.iflove.api.user.dao.UserFriendDao;
import com.iflove.api.user.domain.entity.UserApply;
import com.iflove.api.user.domain.entity.UserFriend;
import com.iflove.api.user.domain.vo.request.friend.FriendApplyReq;
import com.iflove.api.user.domain.vo.response.friend.FriendApplyResp;
import com.iflove.api.user.service.FriendService;
import com.iflove.api.user.service.adapter.FriendAdapter;
import com.iflove.common.domain.vo.request.PageBaseReq;
import com.iflove.common.domain.vo.response.PageBaseResp;
import com.iflove.common.domain.vo.response.RestBean;
import com.iflove.common.event.UserApplyEvent;
import com.iflove.common.exception.FriendErrorEnum;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Service
public class FriendServiceImpl implements FriendService {
    @Resource
    UserFriendDao userFriendDao;
    @Resource
    UserApplyDao userApplyDao;
    @Resource
    ApplicationEventPublisher applicationEventPublisher;

    /**
     * 申请好友
     * @param uid     uid
     * @param request 请求
     * @return {@link RestBean}
     */
    @Transactional
    @Override
    public RestBean<Void> apply(Long uid, FriendApplyReq request) {
        // 不能添加自己为好友
        if (Objects.equals(uid, request.getTargetUid())) {
            return RestBean.failure(FriendErrorEnum.SELF_APPLY_FORBIDDEN);
        }
        // 判断是否存在好友关系
        UserFriend isFriend = userFriendDao.getByFriend(uid, request.getTargetUid());
        // 已经存在好友关系
        if (Objects.nonNull(isFriend)) {
            return RestBean.failure(FriendErrorEnum.ALREADY_FRIENDS);
        }
        // 判断是否存在申请记录 （我 -> 对方）
        UserApply myFriendApply = userApplyDao.getFriendApply(uid, request.getTargetUid());
        if (Objects.nonNull(myFriendApply)) {
            return RestBean.failure(FriendErrorEnum.EXISTS_FRIEND_APPLY);
        }
        // 判断是否存在申请记录 (对方 -> 我)
        UserApply friendApply = userApplyDao.getFriendApply(request.getTargetUid(), uid);
        // 如果存在，直接同意
        if (Objects.nonNull(friendApply)) {
            // TODO 同意申请
        }
        UserApply userApply = FriendAdapter.buildFriendApply(uid, request);
        userApplyDao.save(userApply);
        // 用户申请事件，向对方异步发送请求消息
        applicationEventPublisher.publishEvent(new UserApplyEvent(this, userApply));
        return RestBean.success();
    }

    /**
     * 获取好友申请列表
     * @param uid 用户id
     * @param request 基础翻页请求
     * @return {@link RestBean}<{@link PageBaseResp}<{@link FriendApplyResp}>
     */
    @Override
    public RestBean<PageBaseResp<FriendApplyResp>> pageApplyFriend(Long uid, PageBaseReq request) {
        IPage<UserApply> friendApplyPage = userApplyDao.getFriendApplyPage(uid, request.plusPage());
        if (CollectionUtil.isEmpty(friendApplyPage.getRecords())) {
            return RestBean.success(PageBaseResp.empty());
        }
        // 设置已读
        readApplies(uid, friendApplyPage);
        // 返回数据
        return RestBean.success(
                PageBaseResp.init(friendApplyPage, FriendAdapter.buildFriendApplyList(friendApplyPage.getRecords()))
        );
    }

    private void readApplies(Long uid, IPage<UserApply> page) {
        List<Long> applyIds = page.getRecords()
                .stream()
                .map(UserApply::getId)
                .collect(Collectors.toList());
        userApplyDao.readApplies(uid, applyIds);
    }
}
