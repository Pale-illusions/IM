package com.iflove.api.chat.domain.entity.msg;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 系统消息传输
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemMsgDTO implements Serializable {
    @Schema(description = "系统消息")
    @NotBlank(message = "内容不能为空")
    @Size(max = 1024, message = "消息内容过长")
    private String content;
}
