package com.iflove.api.chat.domain.vo.request.admin;

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
 * @implNote 添加管理员请求体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "添加管理员请求体")
public class AdminAddReq {
    @Schema(description = "房间id")
    @NotNull
    private Long roomId;

    @Schema(description = "新管理员列表")
    @NotNull
    @Size(min = 1)
    private List<Long> uidList;
}
