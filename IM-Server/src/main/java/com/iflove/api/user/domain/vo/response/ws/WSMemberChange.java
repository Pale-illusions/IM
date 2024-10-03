package com.iflove.api.user.domain.vo.response.ws;

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
 * @implNote 群聊成员变更信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WSMemberChange implements Serializable {
    public static final Integer CHANGE_TYPE_ADD = 1;

    public static final Integer CHANGE_TYPE_REMOVE = 2;

    @Schema(description = "群组id")
    private Long roomId;

    @Schema(description = "变动uid")
    private Long uid;

    @Schema(description = "变动类型 1加入群组 2移除群组")
    private Integer changeType;

    /**
     * @see com.iflove.api.user.domain.enums.ChatActiveStatusEnum
     */
    @Schema(description = "在线状态 1在线 2离线")
    private Integer activeStatus;

    @Schema(description = "最后一次上下线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastOptTime;
}
