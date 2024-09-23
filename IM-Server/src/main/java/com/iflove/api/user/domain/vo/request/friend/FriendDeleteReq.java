package com.iflove.api.user.domain.vo.request.friend;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 好友删除请求
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "好友删除请求")
public class FriendDeleteReq {
    @NotNull
    @Schema(description = "好友id")
    private Long targetUid;
}
