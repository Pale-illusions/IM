package com.iflove.api.chat.service;

import com.iflove.api.chat.domain.entity.Message;
import com.iflove.api.chat.domain.vo.request.msg.ChatMessagePageReq;
import com.iflove.api.chat.domain.vo.request.msg.ChatMessageReq;
import com.iflove.api.chat.domain.vo.response.ChatMessageResp;
import com.iflove.common.domain.vo.response.CursorPageBaseResp;
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

    /**
     * 消息返回体
     * @param msgId 消息id
     * @return {@link ChatMessageResp}
     */
    ChatMessageResp getMsgResp(Long msgId);

    /**
     * 消息返回体
     * @param message 消息对象
     * @return {@link ChatMessageResp}
     */
    ChatMessageResp getMsgResp(Message message);

    /**
     * 消息列表
     * @param req 消息列表游标分页请求
     * @param uid 用户id
     * @return {@link RestBean}<{@link CursorPageBaseResp}<{@link ChatMessageResp}
     */
    RestBean<CursorPageBaseResp<ChatMessageResp>> getMsgPage(ChatMessagePageReq req, Long uid);

    /**
     * 消息阅读上报
     * @param roomId 房间id
     * @param uid 用户id
     * @return {@link RestBean}
     */
    RestBean<Void> msgRead(Long roomId, Long uid);
}
