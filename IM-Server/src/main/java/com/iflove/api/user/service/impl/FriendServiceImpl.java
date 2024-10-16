package com.iflove.api.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.iflove.api.chat.domain.entity.RoomFriend;
import com.iflove.api.chat.service.ChatService;
import com.iflove.api.chat.service.RoomService;
import com.iflove.api.chat.service.adapter.MessageAdapter;
import com.iflove.api.user.dao.UserApplyDao;
import com.iflove.api.user.dao.UserDao;
import com.iflove.api.user.dao.UserFriendDao;
import com.iflove.api.user.domain.entity.User;
import com.iflove.api.user.domain.entity.UserApply;
import com.iflove.api.user.domain.entity.UserFriend;
import com.iflove.api.user.domain.enums.ApplyStatusEnum;
import com.iflove.api.user.domain.vo.request.friend.FriendApplyApproveReq;
import com.iflove.api.user.domain.vo.request.friend.FriendApplyDisapproveReq;
import com.iflove.api.user.domain.vo.request.friend.FriendApplyReq;
import com.iflove.api.user.domain.vo.request.friend.FriendCheckReq;
import com.iflove.api.user.domain.vo.response.friend.FriendApplyResp;
import com.iflove.api.user.domain.vo.response.friend.FriendApplyUnreadResp;
import com.iflove.api.user.domain.vo.response.friend.FriendCheckResp;
import com.iflove.api.user.domain.vo.response.friend.FriendInfoResp;
import com.iflove.api.user.service.FriendService;
import com.iflove.api.user.service.adapter.FriendAdapter;
import com.iflove.common.annotation.RedissonLock;
import com.iflove.common.domain.vo.request.CursorPageBaseReq;
import com.iflove.common.domain.vo.request.PageBaseReq;
import com.iflove.common.domain.vo.response.CursorPageBaseResp;
import com.iflove.common.domain.vo.response.PageBaseResp;
import com.iflove.common.domain.vo.response.RestBean;
import com.iflove.common.event.UserApplyEvent;
import com.iflove.common.exception.FriendErrorEnum;
import jakarta.annotation.Resource;
import org.springframework.aop.framework.AopContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Service
public class FriendServiceImpl implements FriendService {
    @Resource
    private UserFriendDao userFriendDao;
    @Resource
    private UserApplyDao userApplyDao;
    @Resource
    private UserDao userDao;
    @Resource
    private RoomService roomService;
    @Resource
    private ChatService chatService;
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * 申请好友
     * @param uid     uid
     * @param request 请求
     * @return {@link RestBean}
     */
    @Transactional
    @Override
    @RedissonLock(key = "#uid")
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
        // 判断是否存在申请记录 （我 -> 对方） 且 申请状态为 待审批
        UserApply myFriendApply = userApplyDao.getFriendApply(uid, request.getTargetUid());
        // 存在申请
        if (Objects.nonNull(myFriendApply)) {
            return RestBean.failure(FriendErrorEnum.EXISTS_FRIEND_APPLY);
        }
        // 判断是否存在申请记录 (对方 -> 我)
        UserApply friendApply = userApplyDao.getFriendApply(request.getTargetUid(), uid);
        // 如果存在，直接同意
        if (Objects.nonNull(friendApply)) {
            // 获取当前执行的对象的代理实例(确保事务正确执行)，同意申请
            ((FriendService) AopContext.currentProxy()).applyApprove(uid, new FriendApplyApproveReq(friendApply.getId()));
            return RestBean.success();
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

    /**
     * 好友申请未读数
     * @return {@link RestBean}
     */
    @Override
    public RestBean<FriendApplyUnreadResp> unread(Long uid) {
        Long unreadCount = userApplyDao.getUnreadCount(uid);
        return RestBean.success(new FriendApplyUnreadResp(unreadCount));
    }

    /**
     * 同意好友申请
     * @param uid 被请求方id
     * @param req 好友申请同意请求
     * @return {@link RestBean}
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    @RedissonLock(key = "#uid")
    public RestBean<Void> applyApprove(Long uid, FriendApplyApproveReq req) {
        // 注意：userApply 的 targetId 为 被请求方， userId 为 请求发起方
        UserApply userApply = userApplyDao.getById(req.getApplyId());
        // 好友申请不存在
        if (Objects.isNull(userApply) || !Objects.equals(userApply.getTargetId(), uid)) {
            return RestBean.failure(FriendErrorEnum.MISSING_FRIEND_APPLY);
        }
        // 已同意或已拒绝
        if (!Objects.equals(userApply.getStatus(), ApplyStatusEnum.WAIT_APPROVAL.getCode())) {
            return RestBean.failure(FriendErrorEnum.OPERATION_FAILURE);
        }
        // 同意申请
        userApplyDao.applyApprove(req.getApplyId());
        // 创建好友关系
        createFriends(uid, userApply.getUserId());
        // 创建单聊房间
        RoomFriend roomFriend = roomService.createRoomFriend(List.of(uid, userApply.getUserId()));
        // 发送聊天初始消息
        chatService.sendMsg(MessageAdapter.buildApplyApproveMsg(roomFriend.getRoomId()), uid);
        return RestBean.success();
    }

    private void createFriends(Long uid, Long targetId) {
        UserFriend relation1 = UserFriend.builder()
                .userId(uid)
                .friendId(targetId)
                .build();
        UserFriend relation2 = UserFriend.builder()
                .userId(targetId)
                .friendId(uid)
                .build();
        userFriendDao.saveBatch(Lists.newArrayList(relation1, relation2));
    }

    /**
     * 拒绝好友申请
     * @param req 好友申请拒绝请求
     * @return {@link RestBean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedissonLock(key = "#uid")
    public RestBean<Void> applyDisapprove(Long uid, FriendApplyDisapproveReq req) {
        UserApply userApply = userApplyDao.getById(req.getApplyId());
        // 好友申请不存在
        if (Objects.isNull(userApply) || !Objects.equals(userApply.getTargetId(), uid)) {
            return RestBean.failure(FriendErrorEnum.MISSING_FRIEND_APPLY);
        }
        // 已同意或已拒绝
        if (!Objects.equals(userApply.getStatus(), ApplyStatusEnum.WAIT_APPROVAL.getCode())) {
            return RestBean.failure(FriendErrorEnum.OPERATION_FAILURE);
        }
        // 拒绝申请
        userApplyDao.applyDisapprove(req.getApplyId());
        return RestBean.success();
    }

    /**
     * 删除好友
     * @param uid 用户id
     * @param targetUid 好友id
     * @return {@link RestBean}
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestBean<Void> deleteFriend(Long uid, Long targetUid) {
        // 获取 (我 -> 对方) 和 (对方 -> 我) 好友关系
        List<UserFriend> relations = userFriendDao.getUserFriend(uid, targetUid);
        // 好友关系不存在
        if (CollectionUtil.isEmpty(relations)) {
            return RestBean.failure(FriendErrorEnum.FRIEND_NOT_EXIST);
        }
        List<Long> relationsId = relations
                .stream()
                .map(UserFriend::getId)
                .collect(Collectors.toList());
        // 删除好友
        userFriendDao.removeByIds(relationsId);
        // 删除单聊房间
        roomService.disableRoomFriend(List.of(uid, targetUid));
        return RestBean.success();
    }

    /**
     * 好友列表 (游标分页)
     * @param req 游标分页请求
     * @return {@link RestBean}<{@link CursorPageBaseResp}<{@link FriendInfoResp}
     */
    @Override
    public RestBean<CursorPageBaseResp<FriendInfoResp>> friendList(Long uid, CursorPageBaseReq req) {
        // 查询好友列表
        CursorPageBaseResp<UserFriend> friendPage = userFriendDao.getFriendPage(uid, req);
        if (CollectionUtil.isEmpty(friendPage.getList())) {
            return RestBean.success(CursorPageBaseResp.empty());
        }
        List<Long> friendIds = friendPage.getList()
                .stream()
                .map(UserFriend::getFriendId)
                .collect(Collectors.toList());
        List<User> userList = userDao.getFriendList(friendIds);
        return RestBean.success(
            CursorPageBaseResp.init(friendPage, FriendAdapter.buildFriendList(friendPage.getList(), userList))
        );
    }

    /**
     * 批量判断是否是自己的好友
     * @param uid 用户id
     * @param req 请求
     * @return {@link RestBean}<{@link FriendCheckResp}
     */
    @Override
    public RestBean<FriendCheckResp> check(Long uid, FriendCheckReq req) {
        List<UserFriend> friendList = userFriendDao.getByFriends(uid, req.getUidList());

        Set<Long> friendIdSet = friendList.stream().map(UserFriend::getFriendId).collect(Collectors.toSet());
        List<FriendCheckResp.FriendCheck> friendCheckList = req.getUidList().stream()
                .map(id -> {
                    FriendCheckResp.FriendCheck friendCheck = new FriendCheckResp.FriendCheck();
                    friendCheck.setIsFriend(friendIdSet.contains(id));
                    friendCheck.setUid(id);
                    return friendCheck;
                })
                .collect(Collectors.toList());
        return RestBean.success(
                new FriendCheckResp(friendCheckList)
        );
    }
}
