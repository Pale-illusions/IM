package com.iflove.api.chat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.iflove.api.chat.dao.ContactDao;
import com.iflove.api.chat.dao.MessageDao;
import com.iflove.api.chat.domain.dto.RoomBaseInfo;
import com.iflove.api.chat.domain.entity.*;
import com.iflove.api.chat.domain.enums.RoomTypeEnum;
import com.iflove.api.chat.domain.vo.response.ChatRoomResp;
import com.iflove.api.chat.service.RoomAppService;
import com.iflove.api.chat.service.RoomService;
import com.iflove.api.chat.service.cache.RoomCache;
import com.iflove.api.chat.service.cache.RoomFriendCache;
import com.iflove.api.chat.service.cache.RoomGroupCache;
import com.iflove.api.chat.service.strategy.AbstractMsgHandler;
import com.iflove.api.chat.service.strategy.MsgHandlerFactory;
import com.iflove.api.user.domain.entity.User;
import com.iflove.api.user.service.cache.UserInfoCache;
import com.iflove.common.domain.vo.request.CursorPageBaseReq;
import com.iflove.common.domain.vo.response.CursorPageBaseResp;
import com.iflove.common.domain.vo.response.RestBean;
import com.iflove.common.exception.FriendErrorEnum;
import com.iflove.common.exception.RoomErrorEnum;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

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
                CursorPageBaseResp.init(
                        contactPage,
                        this.buildContactResp(
                                uid,
                                contactPage.getList()
                                        .stream()
                                        .map(Contact::getRoomId)
                                        .collect(Collectors.toList())
                        )
                )
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
