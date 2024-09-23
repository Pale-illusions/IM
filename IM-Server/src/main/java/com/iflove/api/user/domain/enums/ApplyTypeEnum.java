package com.iflove.api.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 用户申请枚举类
 */
@AllArgsConstructor
@Getter
public enum ApplyTypeEnum {
    ADD_FRIEND(1, "加好友"),
    ;

    private final Integer code;

    private final String desc;
}
