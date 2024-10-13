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
public enum CommentTypeEnum {
    VIDEO(0, "视频"),
    COMMENT(1, "评论"),
    ;

    private final Integer type;
    private final String desc;


    private static final Map<Integer, CommentTypeEnum> cache;

    static {
        cache = Arrays.stream(CommentTypeEnum.values()).collect(Collectors.toMap(CommentTypeEnum::getType, Function.identity()));
    }

    public static CommentTypeEnum of(Integer type) {
        return cache.get(type);
    }
}
