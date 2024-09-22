package com.iflove.api.user.service;

import com.iflove.api.user.domain.vo.response.ws.WSBaseResp;

import java.util.List;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
public interface PushService {
    /**
     * 多向推送
     * @param msg 消息
     * @param uidList 目标用户列表
     */
    void sendPushMsg(WSBaseResp<?> msg, List<Long> uidList);
    /**
     * 单向推送
     * @param msg 消息
     * @param uid 目标用户
     */
    void sendPushMsg(WSBaseResp<?> msg, Long uid);
    /**
     * 全体推送
     * @param msg 消息
     */
    void sendPushMsg(WSBaseResp<?> msg);
}
