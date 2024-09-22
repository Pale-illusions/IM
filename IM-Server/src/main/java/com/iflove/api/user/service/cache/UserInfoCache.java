package com.iflove.api.user.service.cache;

import com.iflove.api.user.dao.UserDao;
import com.iflove.api.user.domain.entity.User;
import com.iflove.common.constant.RedisKey;
import com.iflove.common.service.cache.AbstractRedisStringCache;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Component
public class UserInfoCache extends AbstractRedisStringCache<Long, User> {
    @Resource
    UserDao userDao;

    @Override
    protected String getKey(Long req) {
        return RedisKey.getKey(RedisKey.USER_INFO_STRING, req);
    }

    @Override
    protected Long getExpireSeconds() {
        return 5 * 60L;
    }

    @Override
    protected Map<Long, User> load(List<Long> req) {
        List<User> users = userDao.listByIds(req);
        return users.stream().collect(Collectors.toMap(User::getId, Function.identity()));
    }
}
