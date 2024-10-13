package com.iflove.api.video.domain.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Schema(description = "发布视频请求")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoPublishReq {
    /**
     * 视频标题
     */
    @Schema(description = "视频标题")
    @NotBlank
    private String title;

    /**
     * 视频描述
     */
    @Schema(description = "视频描述")
    @Length(max = 1024)
    private String description;

    /**
     * 视频地址
     */
    @Schema(description = "视频地址")
    @NotBlank
    private String url;

    /**
     * 视频标签
     */
    @Schema(description = "视频标签")
    @Size(min = 1, max = 10)
    @NotNull
    private List<String> tags;
}
