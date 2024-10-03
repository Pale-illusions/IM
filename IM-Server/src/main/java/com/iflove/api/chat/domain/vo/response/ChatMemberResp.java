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
 * @implNote 群成员列表的成员信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "群成员列表的成员信息")
public class ChatMemberResp {
    @Schema(description = "成员id")
    private Long uid;

    /**
     * @see com.iflove.api.user.domain.enums.ChatActiveStatusEnum
     */
    @Schema(description = "在线状态 0 在线 / 1 离线")
    private Integer activeStatus;

    /**
     * @see com.iflove.api.chat.domain.enums.GroupRoleEnum
     */
    @Schema(description = "用户权限")
    private Integer role;

    @Schema(description = "最后一次上下线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastOptTime;
}
