package com.iflove.api.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.iflove.api.user.domain.entity.UserRole;
import com.iflove.api.user.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author IFLOVE
* @description 针对表【user_role(用户权限表)】的数据库操作Service实现
* @createDate 2024-09-17 14:57:36
*/
@Service
public class UserRoleDao extends ServiceImpl<UserRoleMapper, UserRole> {
    // TODO 用户权限
}




