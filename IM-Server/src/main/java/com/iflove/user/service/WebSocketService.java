package com.iflove.user.service;

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
}
