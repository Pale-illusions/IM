package com.iflove.user.domain.vo.response.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Data
public class UserLoginInfoResp {
    private Long id;

    private String name;

    private String avatar;

    private Integer sex;

    private String token;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireTime;
}
