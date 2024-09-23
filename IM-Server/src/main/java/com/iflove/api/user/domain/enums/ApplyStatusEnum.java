package com.iflove.api.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 请求状态枚举类
 */
@Getter
@AllArgsConstructor
public enum ApplyStatusEnum {
    WAIT_APPROVAL(0, "待审批"),
    AGREE(1, "同意"),
    DISAGREE(2, "拒绝")
    ;

    private final Integer code;

    private final String desc;
}
