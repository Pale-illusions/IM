package com.iflove.api.social.domain.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 关注信息返回体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "关注信息")
public class FollowInfoResp {
    @Schema(description = "用户id")
    private Long id;

    @Schema(description = "关注时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date followDate;
}
