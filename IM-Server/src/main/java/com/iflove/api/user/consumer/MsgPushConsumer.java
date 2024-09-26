package com.iflove.api.user.consumer;

import com.iflove.api.user.domain.enums.WSPushTypeEnum;
import com.iflove.api.user.service.WebSocketService;
import com.iflove.common.domain.dto.PushMessageDTO;
import jakarta.annotation.Resource;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;

import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import static com.iflove.common.constant.MQConstant.*;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 响应消息推送消息队列
 */
@RocketMQMessageListener(topic = PUSH_TOPIC, consumerGroup = PUSH_GROUP, messageModel = MessageModel.BROADCASTING)
@Component
public class MsgPushConsumer implements RocketMQListener<PushMessageDTO> {
    @Resource
    WebSocketService webSocketService;

    @Override
    public void onMessage(PushMessageDTO message) {
        WSPushTypeEnum type = WSPushTypeEnum.of(message.getPushType());
        switch (type) {
            case USER -> {
                message.getUidList().forEach(uid -> {
                    webSocketService.sendToUid(message.getWsBaseResp(), uid);
                });
            }
            case ALL -> {
                webSocketService.sendToAllOnline(message.getWsBaseResp());
            }
        }
    }
}
