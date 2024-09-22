package com.iflove.api.chat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflove.api.chat.domain.entity.Message;
import com.iflove.api.chat.mapper.MessageMapper;
import org.springframework.stereotype.Service;

/**
* @author IFLOVE
* @description 针对表【message(消息表)】的数据库操作Service实现
* @createDate 2024-09-17 14:57:36
*/
@Service
public class MessageDao extends ServiceImpl<MessageMapper, Message> {

}




