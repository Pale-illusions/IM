package com.iflove.api.user.domain.vo.request.friend;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 好友申请拒绝请求
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "好友申请拒绝请求")
public class FriendApplyDisapproveReq {
    @Schema(description = "好友申请id")
    @NotNull
    private Long applyId;
}
