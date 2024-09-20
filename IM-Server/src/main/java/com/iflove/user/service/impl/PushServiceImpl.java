package com.iflove.user.service.impl;

import com.iflove.common.constant.MQConstant;
import com.iflove.common.domain.dto.PushMessageDTO;
import com.iflove.common.service.MQ.MQProducer;
import com.iflove.user.domain.vo.response.ws.WSBaseResp;
import com.iflove.user.service.PushService;
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
        mqProducer.sendMessage(MQConstant.MSG_PUSH_QUEUE, new PushMessageDTO(msg, uidList));
    }

    /**
     * 单向推送
     * @param msg 消息
     * @param uid 目标用户
     */
    @Override
    public void sendPushMsg(WSBaseResp<?> msg, Long uid) {
        mqProducer.sendMessage(MQConstant.MSG_PUSH_QUEUE, new PushMessageDTO(msg, uid));
    }

    /**
     * 全体推送
     * @param msg 消息
     */
    @Override
    public void sendPushMsg(WSBaseResp<?> msg) {
        mqProducer.sendMessage(MQConstant.MSG_PUSH_QUEUE, new PushMessageDTO(msg));
    }
}
