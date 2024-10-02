package com.iflove.common.exception;

import lombok.AllArgsConstructor;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 会话错误枚举
 */
@AllArgsConstructor
public enum RoomErrorEnum implements ErrorEnum {
    ROOM_NOT_EXIST(30001, "房间号有误"),
    GROUP_NUMBER_INSUFFICIENT(30002, "群聊建群人数必须大于2"),
    ;

    private final Integer code;
    private final String msg;

    @Override
    public Integer getErrorCode() {
        return code;
    }

    @Override
    public String getErrorMsg() {
        return msg;
    }
}
