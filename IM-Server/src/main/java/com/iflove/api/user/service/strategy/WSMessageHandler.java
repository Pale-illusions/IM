package com.iflove.api.user.service.strategy;

import com.iflove.api.chat.domain.vo.response.ChatMessageResp;
import com.iflove.api.user.dao.OfflineMessageDao;
import com.iflove.api.user.domain.entity.OfflineMessage;
import com.iflove.api.user.domain.enums.WSRespTypeEnum;
import com.iflove.api.user.domain.vo.response.ws.OfflineMessageExtra;
import com.iflove.api.user.domain.vo.response.ws.WSMessage;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Component
public class WSMessageHandler extends AbstractOfflineMsgHandler<ChatMessageResp> {
    @Resource
    private OfflineMessageDao offlineMessageDao;

    @Override
    WSRespTypeEnum getOfflineMsgTypeEnum() {
        return WSRespTypeEnum.MESSAGE;
    }

    @Override
    protected void saveOfflineMsgExtra(OfflineMessage message, ChatMessageResp body) {
        OfflineMessageExtra extra = Optional.ofNullable(message.getData()).orElse(new OfflineMessageExtra());
        OfflineMessage update = new OfflineMessage();
        update.setId(message.getId());
        update.setData(extra);
        extra.setWsMessage(body);
        offlineMessageDao.updateById(update);
    }
}
