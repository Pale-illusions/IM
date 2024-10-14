package com.iflove.api.interactive.domain.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "点赞/点踩列表响应")
public class ActionListResp {
    @Schema(description = "视频id")
    private Long videoId;
}
