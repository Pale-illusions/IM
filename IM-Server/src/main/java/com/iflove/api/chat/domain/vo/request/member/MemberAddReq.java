package com.iflove.api.chat.domain.vo.request.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 添加群成员请求体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "添加群成员请求体")
public class MemberAddReq {
    @Schema(description = "房间id")
    @NotNull
    private Long roomId;

    @Schema(description = "邀请的uid")
    @Size(min = 1, max = 50)
    @NotNull
    private List<Long> uidList;
}
