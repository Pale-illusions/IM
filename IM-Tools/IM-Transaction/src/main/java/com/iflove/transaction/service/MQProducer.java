package com.iflove.transaction.service;

import cn.hutool.core.util.RandomUtil;
import com.iflove.transaction.annotation.SecureInvoke;
import jakarta.annotation.Resource;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 消息队列工具类
 */

public class MQProducer {
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    public void sendMsg(String topic, Object body) {
        Message<Object> build = MessageBuilder.withPayload(body).build();
        rocketMQTemplate.send(topic, build);
    }

    /**
     * 发送可靠消息
     * @param topic 主题
     * @param body 消息体
     * @param key key
     */
    @SecureInvoke
    public void sendSecureMsg(String topic, Object body, Object key) {
        Message<Object> build = MessageBuilder
                .withPayload(body)
                .setHeader("KEYS", key)
                .build();
        rocketMQTemplate.send(topic, build);
    }
}