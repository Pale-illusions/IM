package com.iflove.api.user.domain.vo.response.ws;

import com.iflove.api.user.domain.enums.WSRespTypeEnum;
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
     * @see WSRespTypeEnum
     */
    private Integer type;
    private T data;
}