package com.iflove.api.chat.dao;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflove.api.chat.domain.entity.Contact;
import com.iflove.api.chat.mapper.ContactMapper;
import com.iflove.common.domain.vo.request.CursorPageBaseReq;
import com.iflove.common.domain.vo.response.CursorPageBaseResp;
import com.iflove.common.utils.CursorUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    public CursorPageBaseResp<Contact> getContactPage(CursorPageBaseReq req, Long uid) {
        return CursorUtils.getCursorPageByMysql(this, req, wrapper -> {
            wrapper.eq(Contact::getUserId, uid);
        }, Contact::getActiveTime);
    }

    public void refreshOrCreateActiveTime(Long roomId, List<Long> memberUidList, Long messageId, Date msgCreateTime) {
        baseMapper.refreshOrCreateActiveTime(roomId, memberUidList, messageId, msgCreateTime);
    }

    public void removeByRoomId(Long roomId, List<Long> uidList) {
        lambdaUpdate()
                .eq(Contact::getRoomId, roomId)
                .in(CollectionUtil.isNotEmpty(uidList), Contact::getUserId, uidList)
                .remove();
    }

    public Contact get(Long uid, Long roomId) {
        return lambdaQuery()
                .eq(Contact::getUserId, uid)
                .eq(Contact::getRoomId, roomId)
                .one();
    }
}




