package com.iflove.api.chat.domain.vo.request.member;

import com.iflove.common.domain.vo.request.CursorPageBaseReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 成员列表请求体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "成员列表请求体")
public class MemberPageReq extends CursorPageBaseReq {
    @Schema(description = "房间号")
    @NotNull
    private Long roomId;
}
