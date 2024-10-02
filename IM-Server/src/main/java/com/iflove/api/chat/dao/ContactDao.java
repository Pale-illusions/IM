package com.iflove.api.chat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflove.api.chat.domain.entity.Contact;
import com.iflove.api.chat.mapper.ContactMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author IFLOVE
* @description 针对表【contact(会话表)】的数据库操作Service实现
* @createDate 2024-09-17 14:57:36
*/
@Service
public class ContactDao extends ServiceImpl<ContactMapper, Contact> {

    public List<Contact> getByRoomIds(List<Long> roomIds, Long uid) {
        return lambdaQuery()
                .in(Contact::getRoomId, roomIds)
                .eq(Contact::getUserId, uid)
                .list();
    }
}




