package com.iflove.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.iflove.user.domain.entity.UserRole;
import com.iflove.user.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author IFLOVE
* @description 针对表【user_role(用户权限表)】的数据库操作Service实现
* @createDate 2024-09-17 14:57:36
*/
@Service
public class UserRoleDao extends ServiceImpl<UserRoleMapper, UserRole> {

}




