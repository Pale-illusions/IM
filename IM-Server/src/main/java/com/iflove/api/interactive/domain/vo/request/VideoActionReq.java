package com.iflove.api.interactive.domain.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
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
@Schema(description = "视频点赞点踩请求")
public class VideoActionReq {

    /**
     * 视频id
     */
    @Schema(description = "视频id")
    @NotNull
    private Long videoId;

    /**
     * 标记类型 1 点赞 / 2 点踩
     * @see com.iflove.api.interactive.domain.enums.MarkTypeEnum
     */
    @Schema(description = "标记类型 1 点赞 / 2 点踩")
    @NotNull
    @Min(1)
    @Max(2)
    private Integer markType;

    /**
     * 动作类型 1 确认 / 2 取消
     * @see com.iflove.api.interactive.domain.enums.ActionTypeEnum
     */
    @Schema(description = "动作类型 1 确认 / 2 取消")
    @Min(1)
    @Max(2)
    private Integer actionType;
}
