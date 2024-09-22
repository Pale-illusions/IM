package com.iflove.api.user.service;

import com.iflove.api.user.domain.vo.response.ws.WSBaseResp;
import io.netty.channel.Channel;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
public interface WebSocketService {
    /**
     * 用户连接
     * @param channel
     */
    void connect(Channel channel);

    /**
     * 用户下线
     * @param channel
     */
    void removed(Channel channel);

    /**
     * 登录校验
     * @param channel
     * @param token
     * @return
     */
    boolean authorize(Channel channel, String token);

    /**
     * 推送消息给指定的对象
     * @param wsBaseResp 消息体
     * @param uid 对象uid
     */
    void sendToUid(WSBaseResp<?> wsBaseResp, Long uid);

    /**
     * 推送消息给所有
     * @param wsBaseResp 消息体
     * @param skipUid 跳过的人
     */
    void sendToAllOnline(WSBaseResp<?> wsBaseResp, Long skipUid);

    /**
     * 推送消息给所有
     * @param wsBaseResp 消息体
     */
    void sendToAllOnline(WSBaseResp<?> wsBaseResp);
}
