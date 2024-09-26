package com.iflove.api.user.service.impl;

import com.iflove.api.user.domain.vo.response.ws.WSBaseResp;
import com.iflove.common.constant.MQConstant;
import com.iflove.common.domain.dto.PushMessageDTO;
import com.iflove.common.service.MQ.MQProducer;
import com.iflove.api.user.service.PushService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Service
public class PushServiceImpl implements PushService {
    @Resource
    MQProducer mqProducer;

    /**
     * 多向推送
     * @param msg 消息
     * @param uidList 目标用户列表
     */
    @Override
    public void sendPushMsg(WSBaseResp<?> msg, List<Long> uidList) {
        mqProducer.sendMsg(MQConstant.PUSH_TOPIC, new PushMessageDTO(msg, uidList));
    }

    /**
     * 单向推送
     * @param msg 消息
     * @param uid 目标用户
     */
    @Override
    public void sendPushMsg(WSBaseResp<?> msg, Long uid) {
        mqProducer.sendMsg(MQConstant.PUSH_TOPIC, new PushMessageDTO(msg, uid));
    }

    /**
     * 全体推送
     * @param msg 消息
     */
    @Override
    public void sendPushMsg(WSBaseResp<?> msg) {
        mqProducer.sendMsg(MQConstant.PUSH_TOPIC, new PushMessageDTO(msg));
    }
}
