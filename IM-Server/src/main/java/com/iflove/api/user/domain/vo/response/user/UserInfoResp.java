package com.iflove.api.user.domain.vo.response.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Data
@Schema(description = "用户信息")
public class UserInfoResp {
    @Schema(description = "用户id")
    private Long id;

    @Schema(description = "用户名")
    private String name;

    @Schema(description = "用户头像")
    private String avatar;

    @Schema(description = "用户性别")
    private Integer sex;

    @Schema(description = "用户最后上下线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastOptTime;

    @Schema(description = "在线状态")
    private Integer status;
}
