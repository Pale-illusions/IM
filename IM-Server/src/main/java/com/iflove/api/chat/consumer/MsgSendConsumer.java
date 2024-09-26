package com.iflove.api.chat.consumer;

import org.springframework.stereotype.Component;

import static com.iflove.common.constant.MQConstant.*;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 响应消息发送消息队列
 */

// TODO 消息发送消息队列
@Component
public class MsgSendConsumer {
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(name = MSG_SEND_QUEUE,
//            durable = "true",
//            arguments = @Argument(name = "x-queue-mode",
//                value = "lazy")),
//            exchange = @Exchange(name = MSG_SEND_EXCHANGE, type = ExchangeTypes.DIRECT),
//            key = {MSG_SEND_KEY}
//    ))
//    public void onMessage() {
//        // TODO 消息发送业务逻辑
//
//    }
}
