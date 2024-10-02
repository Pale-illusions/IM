package com.iflove.api.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iflove.api.chat.domain.entity.Contact;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
* @author IFLOVE
* @description 针对表【contact(会话表)】的数据库操作Mapper
* @createDate 2024-09-17 14:57:36
* @Entity generator.domain.Contact
*/
public interface ContactMapper extends BaseMapper<Contact> {

    void refreshOrCreateActiveTime(@Param("roomId") Long roomId,
                                   @Param("memberUidList") List<Long> memberUidList,
                                   @Param("msgId") Long msgId,
                                   @Param("activeTime") Date activeTime);
}




