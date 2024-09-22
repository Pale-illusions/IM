package com.iflove.api.user.domain.vo.request.user;

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
public class UserRegisterVO {
    @Pattern(regexp = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$", message = "用户名只能包含字母、数字或汉字")
    @Length(min = 1, max = 10, message = "用户名长度应在1到10之间")
    String username;

    @Length(min = 6, max = 20, message = "密码长度应在6到20之间")
    String password;

    @Min(value = 1, message = "性别只能为1或2")
    @Max(value = 2, message = "性别只能为1或2")
    int sex;
}
