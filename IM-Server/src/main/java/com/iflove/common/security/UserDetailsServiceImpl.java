package com.iflove.common.security;

import com.iflove.user.dao.UserDao;
import com.iflove.user.domain.entity.User;
import com.iflove.user.service.cache.UserInfoCache;
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.getUserByName(username);
        if (Objects.isNull(user)) throw new UsernameNotFoundException("用户不存在");
        return new UserDetailsImpl(user);
    }
}
