package com.iflove.api.chat.domain.entity.msg;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 图片消息传输
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ImgMsgDTO extends BaseFileDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "宽度（像素）")
    @NotNull
    private Integer width;

    @Schema(description = "高度（像素）")
    @NotNull
    private Integer height;
}
