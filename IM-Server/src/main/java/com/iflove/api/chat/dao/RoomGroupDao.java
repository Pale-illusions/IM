package com.iflove.api.chat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflove.api.chat.domain.entity.RoomGroup;
import com.iflove.api.chat.mapper.RoomGroupMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author IFLOVE
* @description 针对表【room_group(群聊表)】的数据库操作Service实现
* @createDate 2024-09-17 14:57:36
*/
@Service
public class RoomGroupDao extends ServiceImpl<RoomGroupMapper, RoomGroup> {

    /**
     * 群聊房间列表
     * @param roomIds
     * @return
     */
    public List<RoomGroup> listByRoomIds(List<Long> roomIds) {
        return lambdaQuery()
                .in(RoomGroup::getRoomId, roomIds)
                .list();
    }

    /**
     * 根据房间id获取群聊房间
     * @param roomId
     * @return
     */
    public RoomGroup getByRoomId(Long roomId) {
        return lambdaQuery()
                .eq(RoomGroup::getRoomId, roomId)
                .one();
    }
}




