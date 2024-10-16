package com.iflove.api.chat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.iflove.api.chat.dao.GroupMemberDao;
import com.iflove.api.chat.dao.RoomDao;
import com.iflove.api.chat.dao.RoomFriendDao;
import com.iflove.api.chat.dao.RoomGroupDao;
import com.iflove.api.chat.domain.entity.GroupMember;
import com.iflove.api.chat.domain.entity.Room;
import com.iflove.api.chat.domain.entity.RoomFriend;
import com.iflove.api.chat.domain.entity.RoomGroup;
import com.iflove.api.chat.domain.enums.GroupRoleEnum;
import com.iflove.api.chat.domain.enums.RoomTypeEnum;
import com.iflove.api.chat.service.RoomService;
import com.iflove.api.chat.service.adapter.ChatAdapter;
import com.iflove.api.chat.service.cache.RoomFriendCache;
import com.iflove.api.user.domain.entity.User;
import com.iflove.api.user.service.cache.UserInfoCache;
import com.iflove.common.domain.enums.NormalOrNoEnum;
import jakarta.annotation.Resource;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Service
public class RoomServiceImpl implements RoomService {
    @Resource
    private RoomFriendDao roomFriendDao;
    @Resource
    private RoomDao roomDao;
    @Resource
    private RoomFriendCache roomFriendCache;
    @Resource
    private UserInfoCache userInfoCache;
    @Resource
    private RoomGroupDao roomGroupDao;
    @Resource
    private GroupMemberDao groupMemberDao;

    /**
     * 创建一个单聊房间
     * @param uidList 好友uid
     * @return {@link RoomFriend}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoomFriend createRoomFriend(List<Long> uidList) {
        // 如果 uidList 为空 或 人数不为 2
        if (CollectionUtil.isEmpty(uidList) || uidList.size() != 2) {
            throw new ValidationException("房间创建失败，好友人数错误");
        }
        // 生成 房间key 格式 {较小的uid,较大的uid}
        String key = ChatAdapter.generateRoomKey(uidList);
        // 查询是否已存在room
        RoomFriend roomFriend = roomFriendDao.getByKey(key);
        if (Objects.nonNull(roomFriend)) {
            // 如果存在，且房间禁用，恢复房间
            restoreRoomIfNeed(roomFriend);
        } else {
            // 如果不存在，新建房间
            Room room = createRoom(RoomTypeEnum.FRIEND);
            roomFriend = createRoomFriend(room.getId(), uidList);
        }
        return roomFriend;
    }

    /**
     * 禁用一个单聊房间
     * @param uidList 好友uid
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disableRoomFriend(List<Long> uidList) {
        // 如果 uidList 为空 或 人数不为 2
        if (CollectionUtil.isEmpty(uidList) || uidList.size() != 2) {
            throw new ValidationException("房间禁用失败，好友人数错误");
        }
        // 生成 房间key 格式 {较小的uid,较大的uid}
        String key = ChatAdapter.generateRoomKey(uidList);
        roomFriendDao.disableRoomFriend(key);
        // 删除单聊房间缓存
        roomFriendCache.delete(roomFriendDao.getByKey(key).getRoomId());
    }

    /**
     * 获取好友房间
     * @param uid 用户id
     * @param friendId 好友id
     * @return 好友房间
     */
    @Override
    public RoomFriend getFriendRoom(Long uid, Long friendId) {
        String key = ChatAdapter.generateRoomKey(List.of(uid, friendId));
        return roomFriendDao.getByKey(key);
    }

    /**
     * 创建群聊
     * @param uid 用户id
     * @return 群聊
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoomGroup createRoomGroup(Long uid) {
        // TODO 可能可以做个创建群聊数量限制？
        User user = userInfoCache.get(uid);
        // 创建 房间
        Room room = createRoom(RoomTypeEnum.GROUP);
        // 保存群聊信息
        RoomGroup roomGroup = ChatAdapter.buildRoomGroup(user, room.getId());
        roomGroupDao.save(roomGroup);
        // 保存群主信息
        GroupMember leader = GroupMember.builder()
                .role(GroupRoleEnum.LEADER.getType())
                .groupId(roomGroup.getId())
                .userId(uid)
                .build();
        groupMemberDao.save(leader);
        return roomGroup;
    }

    private RoomFriend createRoomFriend(Long RoomId, List<Long> uidList) {
        RoomFriend insert = ChatAdapter.buildRoomFriend(RoomId, uidList);
        roomFriendDao.save(insert);
        return insert;
    }

    private Room createRoom(RoomTypeEnum roomTypeEnum) {
        Room insert = ChatAdapter.buildRoom(roomTypeEnum);
        roomDao.save(insert);
        return insert;
    }

    private void restoreRoomIfNeed(RoomFriend room) {
        if (Objects.equals(room.getStatus(), NormalOrNoEnum.FORBIDDEN.getStatus())) {
            roomFriendDao.restoreRoom(room.getId());
            // 删除单聊房间缓存
            roomFriendCache.delete(room.getRoomId());
        }
    }
}
