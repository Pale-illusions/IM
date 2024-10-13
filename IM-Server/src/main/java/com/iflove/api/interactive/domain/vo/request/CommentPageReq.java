package com.iflove.api.interactive.domain.vo.request;

import com.iflove.common.domain.vo.request.PageBaseReq;
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
@Schema(description = "评论列表请求")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentPageReq extends PageBaseReq {

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
}
