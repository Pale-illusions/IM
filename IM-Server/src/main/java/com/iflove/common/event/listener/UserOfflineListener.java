package com.iflove.common.event.listener;

import com.iflove.common.event.UserOfflineEvent;
import com.iflove.api.user.dao.UserDao;
import com.iflove.api.user.domain.entity.User;
import com.iflove.api.user.domain.enums.ChatActiveStatusEnum;
import com.iflove.api.user.service.PushService;
import com.iflove.api.user.service.adapter.WSAdapter;
import com.iflove.api.user.service.cache.UserCache;
import jakarta.annotation.Resource;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 用户离线事件监听器
 */
@Component
public class UserOfflineListener {
    @Resource
    UserCache userCache;
    @Resource
    UserDao userDao;
    @Resource
    PushService pushService;

    @Async
    @EventListener(classes = UserOfflineEvent.class)
    public void saveRedisAndPush(UserOfflineEvent event) {
        User user = event.getUser();
        userCache.offline(user.getId(), user.getLastOptTime());
        // 全体推送下线通知
        pushService.sendPushMsg(WSAdapter.buildOfflineNotifyResp(user));
    }

    @Async
    @EventListener(classes = UserOfflineEvent.class)
    public void saveDB(UserOfflineEvent event) {
        User user = event.getUser();
        User update = new User();
        update.setId(user.getId());
        update.setLastOptTime(user.getLastOptTime());
        update.setStatus(ChatActiveStatusEnum.OFFLINE.getStatus());
        userDao.updateById(update);
    }
}
