package com.iflove.user.domain.vo.response.ws;

import lombok.Data;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote ws的基本返回体
 */
@Data
public class WSBaseResp<T> {
    /**
     * ws推送给前端的消息
     * 消息类型定义：
     * @see com.iflove.user.domain.enums.WSRespTypeEnum
     */
    private Integer type;
    private T data;
}