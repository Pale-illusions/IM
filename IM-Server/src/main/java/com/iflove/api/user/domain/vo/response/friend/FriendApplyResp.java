package com.iflove.api.user.domain.vo.response.friend;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 好友申请返回
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "好友申请返回")
public class FriendApplyResp {
    @Schema(description = "申请id")
    private Long applyId;

    @Schema(description = "申请人uid")
    private Long uid;

    @Schema(description = "申请类型 1加好友")
    private Integer type;

    @Schema(description = "申请信息")
    private String msg;

    @Schema(description = "申请状态 0待审批 1同意 2拒绝")
    private Integer status;
}
