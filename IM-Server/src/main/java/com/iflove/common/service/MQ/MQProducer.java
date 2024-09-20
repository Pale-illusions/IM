package com.iflove.common.service.MQ;

import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 消息队列工具类
 */
@Component
public class MQProducer {
    @Resource
    RabbitTemplate rabbitTemplate;

    /**
     * 发送消息到指定的队列
     * @param queueName 队列名
     * @param message 消息
     */
    public void sendMessage(String queueName, String message) {
        rabbitTemplate.convertAndSend(queueName, message);
    }

    /**
     * 发送消息到指定的交换机
     * @param exchangeName 交换机
     * @param routingKey 指定路由键
     * @param message 消息
     */
    public void sendMessageToExchange(String exchangeName, String routingKey, String message) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
    }
}
