package com.iflove.api.chat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Pair;
import com.iflove.api.chat.dao.ContactDao;
import com.iflove.api.chat.dao.GroupMemberDao;
import com.iflove.api.chat.dao.MessageDao;
import com.iflove.api.chat.domain.dto.RoomBaseInfo;
import com.iflove.api.chat.domain.entity.*;
import com.iflove.api.chat.domain.enums.GroupRoleEnum;
import com.iflove.api.chat.domain.enums.RoomTypeEnum;
import com.iflove.api.chat.domain.vo.request.member.GroupCreateReq;
import com.iflove.api.chat.domain.vo.request.member.MemberAddReq;
import com.iflove.api.chat.domain.vo.request.member.MemberDelReq;
import com.iflove.api.chat.domain.vo.request.member.MemberPageReq;
import com.iflove.api.chat.domain.vo.response.ChatMemberResp;
import com.iflove.api.chat.domain.vo.response.ChatRoomResp;
import com.iflove.api.chat.service.ChatService;
import com.iflove.api.chat.service.RoomAppService;
import com.iflove.api.chat.service.RoomService;
import com.iflove.api.chat.service.adapter.MemberAdapter;
import com.iflove.api.chat.service.adapter.RoomAdapter;
import com.iflove.api.chat.service.cache.GroupMemberCache;
import com.iflove.api.chat.service.cache.RoomCache;
import com.iflove.api.chat.service.cache.RoomFriendCache;
import com.iflove.api.chat.service.cache.RoomGroupCache;
import com.iflove.api.chat.service.helper.ChatMemberHelper;
import com.iflove.api.chat.service.strategy.AbstractMsgHandler;
import com.iflove.api.chat.service.strategy.MsgHandlerFactory;
import com.iflove.api.user.dao.UserDao;
import com.iflove.api.user.domain.entity.User;
import com.iflove.api.user.domain.enums.ChatActiveStatusEnum;
import com.iflove.api.user.service.cache.UserInfoCache;
import com.iflove.common.domain.vo.request.CursorPageBaseReq;
import com.iflove.common.domain.vo.response.CursorPageBaseResp;
import com.iflove.common.domain.vo.response.RestBean;
import com.iflove.common.event.GroupMemberAddEvent;
import com.iflove.common.event.GroupMemberDelEvent;
import com.iflove.common.exception.FriendErrorEnum;
import com.iflove.common.exception.RoomErrorEnum;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Service
public class RoomAppServiceImpl implements RoomAppService {
    @Resource
    private RoomCache roomCache;
    @Resource
    private RoomGroupCache roomGroupCache;
    @Resource
    private UserInfoCache userInfoCache;
    @Resource
    private RoomFriendCache roomFriendCache;
    @Resource
    private MessageDao messageDao;
    @Resource
    private ContactDao contactDao;
    @Resource
    private RoomService roomService;
    @Resource
    private GroupMemberDao groupMemberDao;
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;
    @Resource
    private GroupMemberCache groupMemberCache;
    @Resource
    private UserDao userDao;

    /**
     * 会话列表
     * @param req 游标分页请求
     * @return {@link RestBean}<{@link CursorPageBaseResp}<{@link ChatRoomResp}
     */
    @Override
    public RestBean<CursorPageBaseResp<ChatRoomResp>> getContactPage(CursorPageBaseReq req, Long uid) {
        // 获取用户会话列表
        CursorPageBaseResp<Contact> contactPage = contactDao.getContactPage(req, uid);
        if (CollectionUtil.isEmpty(contactPage.getList())) {
            return RestBean.success(CursorPageBaseResp.empty());
        }
        // 组装返回
        return RestBean.success(
                CursorPageBaseResp.init(contactPage, this.buildContactResp(uid, contactPage.getList().stream().map(Contact::getRoomId).collect(Collectors.toList())))
        );
    }

    /**
     * 会话详情(房间id)
     * @param roomId 房间id
     * @return {@link RestBean}<{@link ChatRoomResp}
     */
    @Override
    public RestBean<ChatRoomResp> getContactDetail(Long uid, Long roomId) {
        Room room = roomCache.get(roomId);
        // 房间不存在
        if (Objects.isNull(room)) {
            return RestBean.failure(RoomErrorEnum.ROOM_NOT_EXIST);
        }
        return RestBean.success(this.buildContactResp(uid, Collections.singletonList(roomId)).get(0));
    }


    /**
     * 会话详情(好友id)
     * @param friendId 好友id
     * @return {@link RestBean}<{@link ChatRoomResp}
     */
    @Override
    public RestBean<ChatRoomResp> getContactDetailByFriend(Long uid, Long friendId) {
        RoomFriend roomFriend = roomService.getFriendRoom(uid, friendId);
        // 不是好友
        if (Objects.isNull(roomFriend)) {
            return RestBean.failure(FriendErrorEnum.NOT_FRIEND_YET);
        }
        return RestBean.success(this.buildContactResp(uid, Collections.singletonList(roomFriend.getRoomId())).get(0));
    }

    /**
     * 创建群聊
     * @param req 创建请求
     * @return {@link RestBean}<{@link Long}
     */
    @Override
    @Transactional
    public RestBean<Long> createGroup(GroupCreateReq req, Long uid) {
        // TODO 好友过滤
        // 去重且过滤创始人
        List<Long> distinctUidList = req.getUidList().stream().distinct().filter(o -> !Objects.equals(uid, o)).collect(Collectors.toList());
        // 群聊人数必须大于2
        if (CollectionUtil.isEmpty(distinctUidList) || distinctUidList.size() == 1) {
            return RestBean.failure(RoomErrorEnum.GROUP_NUMBER_INSUFFICIENT);
        }
        // 创建群聊
        RoomGroup roomGroup = roomService.createRoomGroup(uid);
        // 批量保存群成员
        List<GroupMember> groupMembers = RoomAdapter.buildGroupMemberBatch(distinctUidList, roomGroup.getId());
        groupMemberDao.saveBatch(groupMembers);
        // 发布群成员添加事件
        applicationEventPublisher.publishEvent(new GroupMemberAddEvent(this, groupMembers, roomGroup, uid));
        return RestBean.success(roomGroup.getRoomId());
    }

    /**
     * 邀请成员
     * @param req 邀请成员请求体
     * @return {@link RestBean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestBean<Void> addMember(MemberAddReq req, Long uid) {
        // TODO 好友过滤
        Room room = roomCache.get(req.getRoomId());
        RoomGroup roomGroup = roomGroupCache.get(req.getRoomId());
        // 房间不存在
        if (Objects.isNull(room) || Objects.isNull(roomGroup)) return RestBean.failure(RoomErrorEnum.ROOM_NOT_EXIST);
        // 不是群成员
        List<Long> memberUidList = groupMemberCache.getMemberUidList(req.getRoomId());
        HashSet<Long> existUids = new HashSet<>(memberUidList);
        if (!existUids.contains(uid)) return RestBean.failure(RoomErrorEnum.NOT_IN_GROUP);
        // 过滤已存在的群成员，并去重
        List<Long> newAddUidList = req.getUidList().stream().filter(o -> !existUids.contains(o)).distinct().collect(Collectors.toList());
        // 新成员集合为空
        if (CollectionUtil.isEmpty(newAddUidList)) return RestBean.success();
        // 保存新成员
        // FIXME 成员不存在, 报错, 后续应做到过滤不存在, 并返回提示信息
        List<GroupMember> groupMembers = RoomAdapter.buildGroupMemberBatch(newAddUidList, roomGroup.getId());
        groupMemberDao.saveBatch(groupMembers);
        // 发布群成员添加事件
        applicationEventPublisher.publishEvent(new GroupMemberAddEvent(this, groupMembers, roomGroup, uid));
        return RestBean.success();
    }

    /**
     * 删除成员
     * @param req 删除成员请求体
     * @return {@link RestBean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestBean<Void> delMember(MemberDelReq req, Long uid) {
        // 不能移除自己
        if (Objects.equals(uid, req.getUid())) return RestBean.failure(RoomErrorEnum.OPERATE_ON_SELF);
        Room room = roomCache.get(req.getRoomId());
        RoomGroup roomGroup = roomGroupCache.get(req.getRoomId());
        // 房间不存在
        if (Objects.isNull(room) || Objects.isNull(roomGroup)) return RestBean.failure(RoomErrorEnum.ROOM_NOT_EXIST);
        // 操作者不是群成员
        GroupMember self = groupMemberDao.getMember(roomGroup.getId(), uid);
        if (Objects.isNull(self)) return RestBean.failure(RoomErrorEnum.NOT_IN_GROUP);
        // 操作对象不是群成员
        GroupMember target = groupMemberDao.getMember(roomGroup.getId(), req.getUid());
        if (Objects.isNull(target)) return RestBean.failure(RoomErrorEnum.MEMBER_NOT_EXIST);
        // 判断权限
        // 1. 群主无法被移除
        if (isLeader(target)) return RestBean.failure(RoomErrorEnum.PERMISSION_NOT_GRANTED);
        // 2. 管理员 判断是否是群主操作
        if (isManager(target) && !isLeader(self)) return RestBean.failure(RoomErrorEnum.PERMISSION_NOT_GRANTED);
        // 3. 普通成员 判断是否有权限操作
        if (!hasPower(self)) return RestBean.failure(RoomErrorEnum.PERMISSION_NOT_GRANTED);
        // 移除 操作对象
        groupMemberDao.removeById(target.getId());
        // 移除 操作对象 会话
        contactDao.removeByRoomIdAndUserId(roomGroup.getRoomId(), target.getUserId());
        // 发布 成员移除 事件
        applicationEventPublisher.publishEvent(new GroupMemberDelEvent(this, target.getUserId(), roomGroup));
        return RestBean.success();
    }

    /**
     * 成员列表
     * @param req 成员列表请求体
     * @return {@link RestBean}<{@link CursorPageBaseResp}<{@link ChatMemberResp}
     */
    @Override
    public RestBean<CursorPageBaseResp<ChatMemberResp>> getMemberPage(MemberPageReq req) {
        Room room = roomCache.get(req.getRoomId());
        // 房间不存在
        if (Objects.isNull(room)) return RestBean.failure(RoomErrorEnum.ROOM_NOT_EXIST);

        RoomGroup roomGroup = roomGroupCache.get(req.getRoomId());
        List<Long> memberUidList = groupMemberDao.getMemberUidList(roomGroup.getId());
        return RestBean.success(this.getMemberPage(memberUidList, req));
    }

    /**
     * 获取成员列表分页
     * 排序方式为 最后操作时间 (降序)
     * 在线列表在上方, 离线列表在下方
     * 在线列表不足以满足分页大小时, 从离线列表补充缺少的部分
     */
    private CursorPageBaseResp<ChatMemberResp> getMemberPage(List<Long> memberUidList, MemberPageReq req) {
        Pair<ChatActiveStatusEnum, String> pair = ChatMemberHelper.getCursorPair(req.getCursor());
        ChatActiveStatusEnum activeStatusEnum = pair.getKey();
        String timeCursor = pair.getValue();
        List<ChatMemberResp> resultList = new ArrayList<>(); // 最终列表
        Boolean isLast = Boolean.FALSE;
        // 查询在线列表
        if (activeStatusEnum == ChatActiveStatusEnum.ONLINE) {
            CursorPageBaseResp<User> cursorPage = userDao.getCursorPage(memberUidList, new CursorPageBaseReq(req.getPageSize(), timeCursor), ChatActiveStatusEnum.ONLINE);
            // 添加在线列表
            resultList.addAll(MemberAdapter.buildMember(cursorPage.getList()));
            // 如果是最后一页, 从离线列表补充缺少的部分
            if (cursorPage.getIsLast()) {
                activeStatusEnum = ChatActiveStatusEnum.OFFLINE;
                Integer leftSize = req.getPageSize() - cursorPage.getList().size();
                cursorPage = userDao.getCursorPage(memberUidList, new CursorPageBaseReq(leftSize, null), ChatActiveStatusEnum.OFFLINE);
                resultList.addAll(MemberAdapter.buildMember(cursorPage.getList()));
            }
            timeCursor = cursorPage.getCursor();
            isLast = cursorPage.getIsLast();
        }
        // 查询离线列表
        else if (activeStatusEnum == ChatActiveStatusEnum.OFFLINE) {
            CursorPageBaseResp<User> cursorPage = userDao.getCursorPage(memberUidList, new CursorPageBaseReq(req.getPageSize(), timeCursor), ChatActiveStatusEnum.OFFLINE);
            // 添加离线列表
            resultList.addAll(MemberAdapter.buildMember(cursorPage.getList()));
            timeCursor = cursorPage.getCursor();
            isLast = cursorPage.getIsLast();
        }
        if (resultList.isEmpty()) return CursorPageBaseResp.empty();
        // 获取群成员角色
        List<Long> uidList = resultList.stream().map(ChatMemberResp::getUid).collect(Collectors.toList());
        RoomGroup roomGroup = roomGroupCache.get(req.getRoomId());
        Map<Long, Integer> uidRoleMap = groupMemberDao.getMemberMapRole(roomGroup.getId(), uidList);
        resultList.forEach(member -> member.setRole(uidRoleMap.get(member.getUid())));
        // 组装结果
        return new CursorPageBaseResp<>(ChatMemberHelper.generateCursor(activeStatusEnum, timeCursor), isLast, resultList);
    }

    /**
     * 判断是否为群主
     */
    private boolean isLeader(GroupMember member) {
        return Objects.equals(member.getRole(), GroupRoleEnum.LEADER.getType());
    }
    /**
     * 判断是否为群管理员
     */
    private boolean isManager(GroupMember member) {
        return Objects.equals(member.getRole(), GroupRoleEnum.MANAGER.getType());
    }
    /**
     * 判断是否拥有操作权限 (即是否为群主或管理员)
     */
    private boolean hasPower(GroupMember member) {
        return GroupRoleEnum.ADMIN_LIST.contains(member.getRole());
    }


    /**
     * 构建会话详情
     * @param uid 用户id
     * @param roomIds 房间id
     * @return {@link ChatRoomResp}
     */
    private List<ChatRoomResp> buildContactResp(Long uid, List<Long> roomIds) {
        // 群聊基本信息
        Map<Long, RoomBaseInfo> roomBaseInfoMap = getRoomBaseInfoMap(roomIds, uid);
        // 最后一条消息
        List<Long> msgIds = roomBaseInfoMap.values().stream().map(RoomBaseInfo::getLastMsgId).collect(Collectors.toList());
        List<Message> messages = CollectionUtil.isEmpty(msgIds) ? new ArrayList<>() : messageDao.listByIds(msgIds);
        Map<Long, Message> msgMap = messages.stream().collect(Collectors.toMap(Message::getId, Function.identity()));
        Map<Long, User> lastMsgUidMap = userInfoCache.getBatch(messages.stream().map(Message::getFromUid).collect(Collectors.toList()));
        // 消息未读数
        Map<Long, Long> unReadCountMap = getUnreadCountMap(roomIds, uid);
        // 装配 会话详情
        return roomBaseInfoMap.values()
                .stream()
                .map(room -> {
                    ChatRoomResp resp = ChatRoomResp.builder()
                        .avatar(room.getAvatar())
                        .roomId(room.getRoomId())
                        .activeTime(room.getActiveTime())
                        .type(room.getType())
                        .name(room.getName())
                        .unreadCount(unReadCountMap.getOrDefault(room.getRoomId(), 0L))
                        .build();
                    Message message = msgMap.get(room.getLastMsgId());
                    if (Objects.nonNull(message)) {
                        AbstractMsgHandler<?> msgHandler = MsgHandlerFactory.getStrategyNonNull(message.getType());
                        resp.setText(lastMsgUidMap.get(message.getFromUid()).getName() + ":" + msgHandler.showContactMsg(message));
                    }
                    return resp;
                })
                .sorted((Comparator.comparing(ChatRoomResp::getActiveTime)).reversed()) // 根据活跃时间排序
                .collect(Collectors.toList());
    }

    /**
     * 获取消息未读数
     * @param roomIds 房间id
     * @param uid 用户id
     * @return 消息未读数
     */
    private Map<Long, Long> getUnreadCountMap(List<Long> roomIds, Long uid) {
        if (Objects.isNull(uid)) {
            return new HashMap<>();
        }
        List<Contact> contacts = contactDao.getByRoomIds(roomIds, uid);
        // 会话数量可能非常大，开启并行处理
        return contacts.parallelStream()
                .map(contact -> Pair.of(contact.getRoomId(), messageDao.getUnReadCount(contact.getRoomId(), contact.getReadTime())))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    /**
     * 获取房间基本信息
     * @param roomIds 房间id
     * @param uid 用户id
     * @return roomId -> RoomBaseInfo
     */
    private Map<Long, RoomBaseInfo> getRoomBaseInfoMap(List<Long> roomIds, Long uid) {
        // 获取 群聊 基本信息
        Map<Long, Room> roomMap = roomCache.getBatch(roomIds);
        // 根据 单聊 和 群聊 进行分组
        Map<Integer, List<Long>> RoomIdTypeMap = roomMap.values()
                .stream()
                .collect(Collectors.groupingBy(Room::getType,
                        Collectors.mapping(Room::getId, Collectors.toList())));
        // 获取 群聊 信息
        List<Long> groupRoomId = RoomIdTypeMap.get(RoomTypeEnum.GROUP.getType());
        Map<Long, RoomGroup> groupRoomMap = roomGroupCache.getBatch(groupRoomId);
        // 获取 单聊 信息
        List<Long> friendRoomId = RoomIdTypeMap.get(RoomTypeEnum.FRIEND.getType());
        Map<Long, User> friendRoomMap = getFriendRoomMap(friendRoomId, uid);
        // 装配返回信息
        return roomMap.values()
                .stream()
                .map(room -> {
                    RoomBaseInfo roomBaseInfo = RoomBaseInfo.builder()
                            .roomId(room.getId())
                            .type(room.getType())
                            .lastMsgId(room.getLastMsgId())
                            .activeTime(room.getActiveTime())
                            .build();
                    // 群聊 设定 群头像和群名称
                    if (RoomTypeEnum.of(room.getType()) == RoomTypeEnum.GROUP) {
                        RoomGroup roomGroup = groupRoomMap.get(room.getId());
                        roomBaseInfo.setAvatar(roomGroup.getAvatar());
                        roomBaseInfo.setName(roomGroup.getName());
                    // 单聊 群头像 为 好友头像，群名称 为 好友名称
                    } else if (RoomTypeEnum.of(room.getType()) == RoomTypeEnum.FRIEND) {
                        User user = friendRoomMap.get(room.getId());
                        roomBaseInfo.setAvatar(user.getAvatar());
                        roomBaseInfo.setName(user.getName());
                    }
                    return roomBaseInfo;
                })
                .collect(Collectors.toMap(RoomBaseInfo::getRoomId, Function.identity()));
    }

    /**
     * 获取好友信息
     * @param roomIds 好友房间id
     * @param uid 用户id
     * @return 好友信息
     */
    private Map<Long, User> getFriendRoomMap(List<Long> roomIds, Long uid) {
        if (CollectionUtil.isEmpty(roomIds)) {
            return new HashMap<>();
        }
        Map<Long, RoomFriend> roomFriendMap = roomFriendCache.getBatch(roomIds);
        // 获取好友 uid 集合
        Set<Long> friendUidSet = roomFriendMap.values()
                .stream()
                .map(a -> Objects.equals(uid, a.getUserId1()) ? a.getUserId2() : a.getUserId1())
                .collect(Collectors.toSet());
        // 获取好友信息
        Map<Long, User> userBatch = userInfoCache.getBatch(new ArrayList<>(friendUidSet));
        return roomFriendMap.values()
                .stream()
                .collect(Collectors.toMap(RoomFriend::getRoomId, roomFriend -> {
                    Long friendUid = Objects.equals(uid, roomFriend.getUserId1()) ? roomFriend.getUserId2() : roomFriend.getUserId1();
                    return userBatch.get(friendUid);
                }));
    }
}
