package com.iflove.api.interactive.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@AllArgsConstructor
@Getter
public enum ActionTypeEnum {
    MARK(1, "确认标记"),
    UN_MARK(2, "取消标记"),
    ;

    private final Integer type;
    private final String desc;

    private static final Map<Integer, ActionTypeEnum> cache;

    static {
        cache = Arrays.stream(ActionTypeEnum.values()).collect(Collectors.toMap(ActionTypeEnum::getType, Function.identity()));
    }

    public static ActionTypeEnum of(Integer type) {
        return cache.get(type);
    }
}
