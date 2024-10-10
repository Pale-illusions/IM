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
 * @implNote 用户权限枚举
 */
@AllArgsConstructor
@Getter
public enum UserRoleEnum {
    NORMAL(0, "member"),
    ADMIN(1, "admin"),
    ;
    private final Integer code;
    private final String desc;

    private static final Map<Integer, String> cache;

    static {
        cache = Arrays.stream(UserRoleEnum.values()).collect(Collectors.toMap(UserRoleEnum::getCode, UserRoleEnum::getDesc));
    }

    public static String of(Integer code) {
        return cache.get(code);
    }
}
