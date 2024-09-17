package com.iflove.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 登录错误 enum
 */
@AllArgsConstructor
public enum LoginErrorEnum implements ErrorEnum{
    UNAUTHENTICATED(10001, "用户未登录"),
    WRONG_USERNAME_OR_PASSWORD(10002, "用户名或密码错误"),
    FREQUENT_LOGOUT_ERROR(10003, "请勿重复操作"),
    ;
    private final Integer code;
    private final String msg;

    @Override
    public Integer getErrorCode() {
        return this.code;
    }

    @Override
    public String getErrorMsg() {
        return this.msg;
    }
}
