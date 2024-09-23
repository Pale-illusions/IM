package com.iflove.oss.domain.vo.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadUrlReq {
    // 文件名（带后缀）
    @NotBlank
    private String fileName;
    // TODO 多场景扩展
    /**
     * 上传场景 1.头像，2.消息
     * @see com.iflove.oss.domain.enums.OssSceneEnum
     */
    @NotNull
    @Min(1)
    @Max(2)
    private Integer scene;
}
