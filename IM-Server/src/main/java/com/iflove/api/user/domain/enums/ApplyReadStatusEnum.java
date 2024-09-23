package com.iflove.api.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Getter
@AllArgsConstructor
public enum ApplyReadStatusEnum {
    UNREAD(0, "未读"),
    READ(1, "已读"),
    ;

    private final Integer code;
    private final String message;
}
