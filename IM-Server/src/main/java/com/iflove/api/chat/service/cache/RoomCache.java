package com.iflove.api.chat.service.cache;

import com.iflove.api.chat.dao.RoomDao;
import com.iflove.api.chat.domain.entity.Room;
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
 * @implNote 房间信息缓存
 */
@Component
public class RoomCache extends AbstractRedisStringCache<Long, Room> {
    @Resource
    private RoomDao roomDao;

    @Override
    protected String getKey(Long roomId) {
        return RedisKey.getKey(RedisKey.ROOM_INFO_STRING, roomId);
    }

    @Override
    protected Long getExpireSeconds() {
        return 5 * 60L;
    }

    @Override
    protected Map<Long, Room> load(List<Long> roomIds) {
        List<Room> rooms = roomDao.listByIds(roomIds);
        return rooms.stream().collect(Collectors.toMap(Room::getId, Function.identity()));
    }
}
