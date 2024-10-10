package com.iflove.api.chat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflove.api.chat.domain.entity.Room;
import com.iflove.api.chat.mapper.RoomMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author IFLOVE
* @description 针对表【room(房间表)】的数据库操作Service实现
* @createDate 2024-09-17 14:57:36
*/
@Service
public class RoomDao extends ServiceImpl<RoomMapper, Room> {
    /**
     * 更新群聊活跃时间
     * @param roomId
     * @param messageId
     * @param msgCreateTime
     */
    public void refreshActiveTime(Long roomId, Long messageId, Date msgCreateTime) {
        lambdaUpdate()
                .eq(Room::getId, roomId)
                .set(Room::getLastMsgId, messageId)
                .set(Room::getActiveTime, msgCreateTime)
                .update();
    }
}




