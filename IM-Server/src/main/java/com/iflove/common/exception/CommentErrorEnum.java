package com.iflove.common.exception;

import lombok.AllArgsConstructor;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@AllArgsConstructor
public enum CommentErrorEnum implements ErrorEnum {
    COMMENT_TARGET_NOT_FOUND(60001, "评论对象不存在"),
    TYPE_NOT_SUPPORTED(60002, "评论类型不支持"),
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
