package com.iflove.api.interactive.domain.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Schema(description = "评论请求")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentPublishReq {

    /**
     * 评论类型 0 视频 / 1 评论
     * @see com.iflove.api.interactive.domain.enums.CommentTypeEnum
     */
    @Schema(description = "评论类型 0 视频 / 1 评论")
    @Min(0)
    @Max(1)
    private Integer type;

    /**
     * 评论对象id
     */
    @Schema(description = "评论对象id")
    @NotNull
    private Long targetId;

    /**
     * 评论内容
     */
    @Schema(description = "评论内容")
    @NotBlank
    private String content;
}
