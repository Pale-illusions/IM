package com.iflove.common.service.MQ;

import jakarta.annotation.Resource;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 消息队列工具类
 */

@Component
public class MQProducer {
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    public void sendMsg(String topic, Object body) {
        Message<Object> build = MessageBuilder.withPayload(body).build();
        rocketMQTemplate.send(topic, build);
    }

    // TODO 实现发送安全消息
}