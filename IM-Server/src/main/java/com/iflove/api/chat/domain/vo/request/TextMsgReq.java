package com.iflove.api.chat.domain.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 文本消息入参
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TextMsgReq {
    @NotBlank(message = "内容不能为空")
    @Size(max = 1024, message = "消息内容过长")
    @Schema(description = "消息内容")
    private String content;
}
