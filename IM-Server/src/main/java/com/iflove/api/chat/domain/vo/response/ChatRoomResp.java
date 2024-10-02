package com.iflove.api.chat.domain.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 会话信息返回体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "会话信息返回体")
public class ChatRoomResp {

    @Schema(description = "房间id")
    private Long roomId;

    @Schema(description = "房间类型 1群聊 2单聊")
    private Integer type;

    @Schema(description = "最新消息")
    private String text;

    @Schema(description = "会话名称")
    private String name;

    @Schema(description = "会话头像")
    private String avatar;

    @Schema(description = "房间最后活跃时间(用来排序)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date activeTime;

    @Schema(description = "未读数")
    private Long unreadCount;
}
