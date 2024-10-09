package com.iflove.api.user.domain.vo.response.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iflove.api.user.domain.entity.IpInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Data
@Schema(description = "用户登录信息")
public class UserLoginInfoResp {
    @Schema(description = "用户id")
    private Long id;

    @Schema(description = "用户名")
    private String name;

    @Schema(description = "用户头像")
    private String avatar;

    @Schema(description = "用户性别")
    private Integer sex;

    @Schema(description = "登录token")
    private String token;

    @Schema(description = "token过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireTime;

    @Schema(description = "IP信息")
    private IpInfo ipInfo;
}
