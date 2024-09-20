package com.iflove.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 聊天状态枚举
 */
@AllArgsConstructor
@Getter
public enum ChatActiveStatusEnum {
    ONLINE(0, "在线"),
    OFFLINE(1, "离线"),
    ;

    private final Integer status;
    private final String desc;

    private static final Map<Integer, ChatActiveStatusEnum> cache;

    static {
        cache = Arrays.stream(ChatActiveStatusEnum.values()).collect(Collectors.toMap(ChatActiveStatusEnum::getStatus, Function.identity()));
    }

    public static ChatActiveStatusEnum of(Integer type) {
        return cache.get(type);
    }
}
