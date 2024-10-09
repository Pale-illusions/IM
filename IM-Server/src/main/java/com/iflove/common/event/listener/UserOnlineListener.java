package com.iflove.common.event.listener;

import com.iflove.common.event.UserOnlineEvent;
import com.iflove.api.user.dao.UserDao;
import com.iflove.api.user.domain.entity.User;
import com.iflove.api.user.domain.enums.ChatActiveStatusEnum;
import com.iflove.api.user.service.PushService;
import com.iflove.api.user.service.adapter.WSAdapter;
import com.iflove.api.user.service.cache.UserCache;
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
        // 全体推送上线通知
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
        userCache.remove(update.getId());
    }
}
