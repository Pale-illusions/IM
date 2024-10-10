package com.iflove.api.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflove.api.user.domain.entity.Role;
import com.iflove.api.user.mapper.RoleMapper;
import org.springframework.stereotype.Service;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Service
public class RoleDao extends ServiceImpl<RoleMapper, Role> {
    public Role getByUserId(Long userId) {
        return lambdaQuery()
                .eq(Role::getUserId, userId)
                .one();
    }
}
