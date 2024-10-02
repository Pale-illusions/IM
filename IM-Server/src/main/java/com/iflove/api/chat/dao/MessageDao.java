package com.iflove.api.chat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflove.api.chat.domain.entity.Message;
import com.iflove.api.chat.domain.enums.MessageStatusEnum;
import com.iflove.api.chat.domain.vo.request.ChatMessagePageReq;
import com.iflove.api.chat.mapper.MessageMapper;
import com.iflove.common.domain.vo.request.CursorPageBaseReq;
import com.iflove.common.domain.vo.response.CursorPageBaseResp;
import com.iflove.common.utils.CursorUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
* @author IFLOVE
* @description 针对表【message(消息表)】的数据库操作Service实现
* @createDate 2024-09-17 14:57:36
*/
@Service
public class MessageDao extends ServiceImpl<MessageMapper, Message> {
    public CursorPageBaseResp<Message> getCursorpage(Long roomId, CursorPageBaseReq req) {
        return CursorUtils.getCursorPageByMysql(this, req, wrapper -> {
            wrapper.eq(Message::getRoomId, roomId);
            wrapper.eq(Message::getStatus, MessageStatusEnum.NORMAL.getStatus());
        }, Message::getId);
    }

    public Long getUnReadCount(Long roomId, Date readTime) {
        return lambdaQuery()
                .eq(Message::getRoomId, roomId)
                .gt(Objects.nonNull(readTime), Message::getCreateTime, readTime)
                .count();
    }
}




