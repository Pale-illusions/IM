package com.iflove.api.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflove.api.user.domain.entity.User;
import com.iflove.api.user.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author IFLOVE
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-09-17 14:57:36
*/
@Service
public class UserDao extends ServiceImpl<UserMapper, User> {
    public User getUserByName(String name) {
        return lambdaQuery()
                .eq(User::getName, name)
                .one();
    }

    public List<User> getFriendList(List<Long> friendIds) {
        return lambdaQuery()
                .in(User::getId, friendIds)
                .select(User::getId, User::getStatus, User::getName, User::getAvatar)
                .list();
    }
}




