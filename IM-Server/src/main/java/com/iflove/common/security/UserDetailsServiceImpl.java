package com.iflove.common.security;

import com.iflove.api.user.dao.RoleDao;
import com.iflove.api.user.dao.UserDao;
import com.iflove.api.user.domain.entity.Role;
import com.iflove.api.user.domain.entity.User;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private UserDao userDao;
    @Resource
    private RoleDao roleDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.getUserByName(username);
        if (Objects.isNull(user)) throw new UsernameNotFoundException("用户不存在");
        Role role = roleDao.getByUserId(user.getId());
        return new UserDetailsImpl(user, role);
    }
}
