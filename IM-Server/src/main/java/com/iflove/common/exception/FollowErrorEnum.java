package com.iflove.common.exception;

import lombok.AllArgsConstructor;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 关注异常枚举
 */
@AllArgsConstructor
public enum FollowErrorEnum implements ErrorEnum {
    OPERATION_REPEAT(40001, "请勿重复操作"),
    OPERATION_ON_SELF(40002, "不能对自己操作"),

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
