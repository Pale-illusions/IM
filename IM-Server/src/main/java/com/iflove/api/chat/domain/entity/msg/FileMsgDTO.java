package com.iflove.api.chat.domain.entity.msg;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 文件消息传输
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class FileMsgDTO extends BaseFileDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "文件名（带后缀）")
    @NotBlank
    private String fileName;
}
