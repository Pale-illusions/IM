package com.iflove.api.chat.domain.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 群组详情返回体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "群组详情返回体")
public class GroupInfoResp {
    @Schema(description = "房间id")
    private Long roomId;

    @Schema(description = "群名称")
    private String groupName;

    @Schema(description = "群头像")
    private String avatar;

    @Schema(description = "在线人数")
    private Long onlineNum;

    /**
     * @see com.iflove.api.chat.domain.enums.GroupRoleEnum
     */
    @Schema(description = "用户角色 1 群主; 2 管理员; 3 普通成员; 4 不在群聊内")
    private Integer role;
}
