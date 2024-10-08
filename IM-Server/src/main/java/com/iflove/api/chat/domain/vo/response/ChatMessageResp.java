package com.iflove.api.chat.domain.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 消息返回体
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "消息返回体")
public class ChatMessageResp implements Serializable {
    @Schema(description = "发送者信息")
    private UserInfo fromUser;
    @Schema(description = "消息详情")
    private Message message;

    @Data
    public static class UserInfo implements Serializable {
        @Schema(description = "用户id")
        private Long uid;
    }

    @Data
    public static class Message implements Serializable {
        @Schema(description = "消息id")
        private Long id;
        @Schema(description = "房间id")
        private Long roomId;
        @Schema(description = "消息发送时间")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date sendTime;
        @Schema(description = "消息类型")
        private Integer type;
        @Schema(description = "消息内容不同的消息类型，内容体不同")
        private Object body;
    }
}
