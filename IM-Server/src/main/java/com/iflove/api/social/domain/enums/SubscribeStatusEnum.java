package com.iflove.api.social.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 关注状态枚举
 */
@AllArgsConstructor
@Getter
public enum SubscribeStatusEnum {
    SUBSCRIBE(0, "关注"),
    UNSUBSCRIBE(1, "未关注"),
    ;

    private final Integer stauts;
    private final String desc;

    private static final Map<Integer, SubscribeStatusEnum> cache;

    static {
        cache = Arrays.stream(SubscribeStatusEnum.values()).collect(Collectors.toMap(SubscribeStatusEnum::getStauts, Function.identity()));
    }

    public static SubscribeStatusEnum of(Integer type) {
        return cache.get(type);
    }
}
