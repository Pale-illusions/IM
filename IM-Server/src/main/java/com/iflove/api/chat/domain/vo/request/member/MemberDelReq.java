package com.iflove.api.chat.domain.vo.request.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 移除群成员请求体
 */
@AllArgsConstructor
@NotNull
@Data
@Builder
@Schema(description = "移除群成员请求体")
public class MemberDelReq {
    @Schema(description = "会话id")
    @NotNull
    private Long roomId;

    @Schema(description = "被移除的uid")
    @NotNull
    private Long uid;
}
