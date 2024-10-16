package com.iflove.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 好友错误枚举
 */
@AllArgsConstructor
public enum FriendErrorEnum implements ErrorEnum{
    ALREADY_FRIENDS(20001, "已经是好友了"),
    EXISTS_FRIEND_APPLY(20002, "已存在好友申请"),
    SELF_APPLY_FORBIDDEN(20003, "不能添加自己为好友"),
    MISSING_FRIEND_APPLY(20004, "好友申请不存在"),
    OPERATION_FAILURE(20005, "操作失败"),
    FRIEND_NOT_EXIST(20006, "好友不存在"),
    NOT_FRIEND_YET(20007, "还不是好友");
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
