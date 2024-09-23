package com.iflove.api.user.domain.vo.request.friend;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "好友申请")
public class FriendApplyReq {
    @NotBlank
    @Schema(description = "申请信息")
    private String msg;

    @NotNull
    @Schema(description = "申请对象")
    private Long targetUid;
}
