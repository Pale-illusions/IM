package com.iflove.user.consumer;

import com.iflove.common.domain.dto.PushMessageDTO;
import com.iflove.user.domain.enums.WSPushTypeEnum;
import com.iflove.user.service.WebSocketService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import static com.iflove.common.constant.MQConstant.*;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 响应消息推送消息队列
 */
@Component
public class MsgPushConsumer {
    @Resource
    WebSocketService webSocketService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = MSG_PUSH_QUEUE,
                    durable = "true",
                    arguments = @Argument(name = "x-queue-mode",
                            value = "lazy")),
            exchange = @Exchange(name = MSG_PUSH_EXCHANGE, type = ExchangeTypes.DIRECT),
            key = {MSG_PUSH_KEY}
    ))
    public void onMessage(PushMessageDTO message) {
        // TODO 消息接收业务逻辑
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
