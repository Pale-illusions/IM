package com.iflove.api.chat.domain.entity.msg;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 文件传输基类
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseFileDTO implements Serializable {
    @Schema(description = "大小（字节）")
    @NotNull
    private Long size;

    @Schema(description = "下载地址")
    @NotBlank
    private String url;

    private static final long serialVersionUID = 1L;
}
