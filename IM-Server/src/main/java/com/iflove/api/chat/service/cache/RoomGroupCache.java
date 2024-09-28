package com.iflove.api.chat.service.cache;

import com.iflove.api.chat.dao.RoomGroupDao;
import com.iflove.api.chat.domain.entity.RoomGroup;
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
 * @implNote 群聊房间信息缓存
 */
@Component
public class RoomGroupCache extends AbstractRedisStringCache<Long, RoomGroup> {
    @Resource
    private RoomGroupDao roomGroupDao;

    @Override
    protected String getKey(Long roomId) {
        return RedisKey.getKey(RedisKey.ROOM_GROUP_STRING, roomId);
    }

    @Override
    protected Long getExpireSeconds() {
        return 5 * 60L;
    }

    @Override
    protected Map<Long, RoomGroup> load(List<Long> roomIds) {
        List<RoomGroup> roomGroups = roomGroupDao.listByRoomIds(roomIds);
        return roomGroups.stream().collect(Collectors.toMap(RoomGroup::getRoomId, Function.identity()));
    }
}
