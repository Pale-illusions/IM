package com.iflove.common.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 状态是否正常枚举类
 */
@AllArgsConstructor
@Getter
public enum NormalOrNoEnum {
    NORMAL(0, "正常"),
    FORBIDDEN(1, "禁用"),
    ;

    private final Integer status;
    private final String desc;

    private static final Map<Integer, NormalOrNoEnum> cache;

    static {
        cache = Arrays.stream(NormalOrNoEnum.values()).collect(Collectors.toMap(NormalOrNoEnum::getStatus, Function.identity()));
    }

    public static NormalOrNoEnum of(Integer type) {
        return cache.get(type);
    }

    public static Integer toStatus(Boolean bool) {
        return bool ? NORMAL.getStatus() : FORBIDDEN.getStatus();
    }
}
