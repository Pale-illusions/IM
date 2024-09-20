package com.iflove.user.consumer;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import static com.iflove.common.constant.MQConstant.*;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 响应消息接收消息队列
 */
@Component
public class MsgPushConsumer {
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = MSG_PUSH_QUEUE,
                    durable = "true",
                    arguments = @Argument(name = "x-queue-mode",
                            value = "lazy")),
            exchange = @Exchange(name = MSG_PUSH_EXCHANGE, type = ExchangeTypes.DIRECT),
            key = {MSG_PUSH_KEY}
    ))
    public void onMessage() {

    }
}
