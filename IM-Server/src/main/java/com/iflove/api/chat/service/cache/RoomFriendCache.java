package com.iflove.api.chat.service.cache;

import com.iflove.api.chat.dao.RoomFriendDao;
import com.iflove.api.chat.domain.entity.RoomFriend;
import com.iflove.common.constant.RedisKey;
import com.iflove.common.service.cache.AbstractRedisStringCache;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 单聊房间信息缓存
 */
@Component
public class RoomFriendCache extends AbstractRedisStringCache<Long, RoomFriend> {
    @Resource
    private RoomFriendDao roomFriendDao;

    @Override
    protected String getKey(Long roomId) {
        return RedisKey.getKey(RedisKey.ROOM_FRIEND_STRING, roomId);
    }

    @Override
    protected Long getExpireSeconds() {
        return 5 * 60L;
    }

    @Override
    protected Map<Long, RoomFriend> load(List<Long> roomIds) {
        List<RoomFriend> roomFriends = roomFriendDao.listByRoomIds(roomIds);
        return roomFriends.stream().collect(Collectors.toMap(RoomFriend::getRoomId, Function.identity()));
    }
}
