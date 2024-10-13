package com.iflove.api.video.domain.vo.request;

import com.iflove.common.domain.vo.request.PageBaseReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 视频搜索请求
 */
@Schema(description = "视频搜索请求")
@Data
@AllArgsConstructor
@NotNull
public class VideoSearchReq extends PageBaseReq {
    @Schema(description = "关键词(关键词和标签不能同时为空)")
    private String keyword;

    @Schema(description = "起始时间")
    private Date fromDate;

    @Schema(description = "终止时间")
    private Date toDate;

    @Schema(description = "标签(关键词和标签不能同时为空)")
    @Size(max = 10)
    private List<String> tags;
}
