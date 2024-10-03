package com.iflove.api.chat.domain.vo.request.msg;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 消息发送请求体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "消息发送请求体")
public class ChatMessageReq {
    /**
     * 房间id
     */
    @Schema(description = "房间id")
    @NotNull
    private Long roomId;

    /**
     * 消息类型
     * @see com.iflove.api.chat.domain.enums.MessageTypeEnum
     */
    @Schema(description = "消息类型")
    @NotNull
    private Integer msgType;

    /**
     * 消息内容，类型不同传值不同
     * @see com.iflove.api.chat.domain.entity.msg
     */
    @Schema(description = "消息内容")
    @NotNull
    private Object body;
}
