package com.iflove.common.event.listener;

import com.iflove.common.constant.MQConstant;
import com.iflove.common.event.UserOnlineEvent;
import com.iflove.common.service.MQ.MQProducer;
import com.iflove.user.dao.UserDao;
import com.iflove.user.domain.entity.User;
import com.iflove.user.domain.enums.ChatActiveStatusEnum;
import com.iflove.user.service.PushService;
import com.iflove.user.service.adapter.WSAdapter;
import com.iflove.user.service.cache.UserCache;
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
    @Resource
    UserCache userCache;
    @Resource
    PushService pushService;

    @Async
    @EventListener(classes = UserOnlineEvent.class)
    public void saveRedisAndPush(UserOnlineEvent event) {
        User user = event.getUser();
        userCache.online(user.getId(), user.getLastOptTime());
        pushService.sendPushMsg(WSAdapter.buildOnlineNotifyResp(user));
    }

    @Async
    @EventListener(classes = UserOnlineEvent.class)
    public void saveDB(UserOnlineEvent event) {
        User user = event.getUser();
        User update = new User();
        update.setId(user.getId());
        update.setLastOptTime(user.getLastOptTime());
        update.setIpInfo(user.getIpInfo());
        update.setStatus(ChatActiveStatusEnum.ONLINE.getStatus());
        userDao.updateById(update);
        // TODO 更新用户IP
    }
}
