package com.iflove.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflove.user.domain.entity.Role;
import com.iflove.user.mapper.RoleMapper;
import org.springframework.stereotype.Service;

/**
* @author IFLOVE
* @description 针对表【role(角色表)】的数据库操作Service实现
* @createDate 2024-09-17 14:57:36
*/
@Service
public class RoleDao extends ServiceImpl<RoleMapper, Role> {

}




