package com.iflove.api.video.domain.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 视频搜索响应
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "视频搜索响应")
public class VideoSearchResp {
    @Schema(description = "视频id")
    private Long id;

    @Schema(description = "视频标题")
    private String title;

    @Schema(description = "地址")
    private String url;

    @Schema(description = "用户id")
    private String userId;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Schema(description = "点击量")
    private Long clickCount;
}
