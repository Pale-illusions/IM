package com.iflove.api.chat.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 消息类型枚举类
 */
@AllArgsConstructor
@Getter
public enum MessageTypeEnum {
    TEXT(1, "文本消息"),
    IMG(2, "图片"),
    FILE(3, "文件"),
    VIDEO(4, "视频"),
    SYSTEM(5, "系统消息"),
    ;

    private final Integer type;
    private final String desc;

    private static final Map<Integer, MessageTypeEnum> cache;

    static {
        cache = Arrays.stream(MessageTypeEnum.values()).collect(Collectors.toMap(MessageTypeEnum::getType, Function.identity()));
    }

    public static MessageTypeEnum of(Integer type) {
        return cache.get(type);
    }
}
