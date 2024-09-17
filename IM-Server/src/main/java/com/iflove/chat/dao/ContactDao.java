package com.iflove.chat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflove.chat.domain.entity.Contact;
import com.iflove.chat.mapper.ContactMapper;
import org.springframework.stereotype.Service;

/**
* @author IFLOVE
* @description 针对表【contact(会话表)】的数据库操作Service实现
* @createDate 2024-09-17 14:57:36
*/
@Service
public class ContactDao extends ServiceImpl<ContactMapper, Contact> {

}




