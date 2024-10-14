package com.iflove.api.interactive.domain.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "点赞/点踩列表响应")
public class ActionListResp {
    @Schema(description = "视频id")
    private Long videoId;

    @Schema(description = "点赞时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date date;
}
