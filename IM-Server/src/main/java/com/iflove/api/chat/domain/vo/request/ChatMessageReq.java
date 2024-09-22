package com.iflove.api.chat.domain.vo.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageReq {
    @NotNull
    private Long roomId;

    @NotNull
    private Integer msgType;

    /**
     * 消息内容，类型不同传值不同
     * @see
     */
    @NotNull
    private Object body;
}
