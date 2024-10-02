package com.iflove.api.chat.domain.vo.request;

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
 * @implNote 创建群聊请求体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "创建群聊请求体")
public class GroupCreateReq {
    @NotNull
    @Size(min = 1, max = 50)
    @Schema(description = "邀请的uid")
    private List<Long> uidList;
}
