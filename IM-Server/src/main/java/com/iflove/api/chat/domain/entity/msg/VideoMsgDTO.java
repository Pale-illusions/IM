package com.iflove.api.chat.domain.entity.msg;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
public class VideoMsgDTO extends BaseFileDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "缩略图宽度（像素）")
    @NotNull
    private Integer thumbWidth;

    @Schema(description = "缩略图高度（像素）")
    @NotNull
    private Integer thumbHeight;

    @Schema(description = "缩略图大小（字节）")
    @NotNull
    private Long thumbSize;

    @Schema(description = "缩略图下载地址")
    @NotBlank
    private String thumbUrl;
}
