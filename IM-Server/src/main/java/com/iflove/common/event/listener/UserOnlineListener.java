package com.iflove.common.event.listener;

import com.iflove.common.event.UserOnlineEvent;
import com.iflove.user.dao.UserDao;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 用户上线监听器
 */
@Slf4j
@Component
public class UserOnlineListener {

    @Resource
    UserDao userDao;

    @Async
    @EventListener(classes = UserOnlineEvent.class)
    public void saveRedisAndPush(UserOnlineEvent event) {

    }

    @Async
    @EventListener(classes = UserOnlineEvent.class)
    public void saveDB(UserOnlineEvent event) {

    }
}
