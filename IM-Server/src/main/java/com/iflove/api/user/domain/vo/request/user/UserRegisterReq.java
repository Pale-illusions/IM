package com.iflove.api.user.domain.vo.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Data
@Schema(description = "用户注册信息")
public class UserRegisterReq {
    @Pattern(regexp = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$", message = "用户名只能包含字母、数字或汉字")
    @Length(min = 1, max = 10, message = "用户名长度应在1到10之间")
    @Schema(description = "用户名")
    String username;

    @Schema(description = "密码")
    @Length(min = 6, max = 20, message = "密码长度应在6到20之间")
    String password;

    @Schema(description = "性别")
    @Min(value = 1, message = "性别只能为1或2")
    @Max(value = 2, message = "性别只能为1或2")
    int sex;
}
