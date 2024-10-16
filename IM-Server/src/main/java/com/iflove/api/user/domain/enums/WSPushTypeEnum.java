package com.iflove.api.user.domain.enums;

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
public enum WSPushTypeEnum {
    USER(1, "个人"),
    ALL(2, "全部在线用户"),
    ;

    private final Integer type;
    private final String desc;

    private static Map<Integer, WSPushTypeEnum> cache;

    static {
        cache = Arrays.stream(WSPushTypeEnum.values()).collect(Collectors.toMap(WSPushTypeEnum::getType, Function.identity()));
    }

    public static WSPushTypeEnum of(Integer type) {
        return cache.get(type);
    }
}
