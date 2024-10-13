package com.iflove.common.exception;

import lombok.AllArgsConstructor;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@AllArgsConstructor
public enum VideoErrorEnum implements ErrorEnum {
    VIDEO_NOT_EXIST(50001, "视频不存在"),
    SEARCH_FAIL(50002, "关键词和标签不能同时为空"),

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
