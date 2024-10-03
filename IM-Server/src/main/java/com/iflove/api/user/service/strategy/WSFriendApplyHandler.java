package com.iflove.api.user.service.strategy;

import com.iflove.api.user.dao.OfflineMessageDao;
import com.iflove.api.user.domain.entity.OfflineMessage;
import com.iflove.api.user.domain.enums.WSRespTypeEnum;
import com.iflove.api.user.domain.vo.response.ws.OfflineMessageExtra;
import com.iflove.api.user.domain.vo.response.ws.WSFriendApply;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Component
public class WSFriendApplyHandler extends AbstractOfflineMsgHandler<WSFriendApply> {
    @Resource
    private OfflineMessageDao offlineMessageDao;

    @Override
    WSRespTypeEnum getOfflineMsgTypeEnum() {
        return WSRespTypeEnum.FRIEND_APPLY;
    }

    @Override
    protected void saveOfflineMsgExtra(OfflineMessage message, WSFriendApply body) {
        OfflineMessageExtra extra = Optional.ofNullable(message.getData()).orElse(new OfflineMessageExtra());
        OfflineMessage update = new OfflineMessage();
        update.setId(message.getId());
        update.setData(extra);
        extra.setWsFriendApply(body);
        offlineMessageDao.updateById(update);
    }
}
