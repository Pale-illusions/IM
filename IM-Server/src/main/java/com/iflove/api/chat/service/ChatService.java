package com.iflove.api.chat.service;

import com.iflove.api.chat.domain.vo.request.ChatMessageReq;
import com.iflove.api.chat.domain.vo.response.ChatMessageResp;
import com.iflove.common.domain.vo.response.RestBean;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
public interface ChatService {
    /**
     * 发送消息
     * @param request 消息请求体
     * @param uid 发送人id
     * @return {@link Long}
     */
    Long sendMsg(ChatMessageReq request, Long uid);
}
