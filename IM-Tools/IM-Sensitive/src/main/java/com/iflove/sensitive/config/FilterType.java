package com.iflove.sensitive.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 过滤算法枚举
 */
@Getter
@AllArgsConstructor
public enum FilterType {
    DFA("DFA", 1),
    AC("AC", 2),
    ;

    private final String name;
    private final Integer type;
}
