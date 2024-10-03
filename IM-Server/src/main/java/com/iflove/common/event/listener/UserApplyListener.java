package com.iflove.common.event.listener;

import com.iflove.api.user.dao.UserApplyDao;
import com.iflove.api.user.domain.entity.UserApply;
import com.iflove.api.user.domain.vo.response.ws.WSFriendApply;
import com.iflove.api.user.service.PushService;
import com.iflove.api.user.service.adapter.WSAdapter;
import com.iflove.common.event.UserApplyEvent;
import jakarta.annotation.Resource;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Component
public class UserApplyListener {
    @Resource
    UserApplyDao userApplyDao;
    @Resource
    PushService pushService;

    @Async
    @TransactionalEventListener(classes = UserApplyEvent.class, fallbackExecution = true)
    public void notifyFriend(UserApplyEvent event) {
        UserApply userApply = event.getUserApply();
        Long unreadCount = userApplyDao.getUnreadCount(userApply.getTargetId());
        pushService.sendPushMsg(WSAdapter.buildApplySend(new WSFriendApply(userApply.getUserId(), unreadCount)), userApply.getTargetId());
    }
}
