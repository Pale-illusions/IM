package com.iflove.sensitive.algorithm.sensitiveWord.ac;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Getter
@Setter
@AllArgsConstructor
public class MatchResult {

    private int startIndex;

    private int endIndex;

    @Override
    public String toString() {
        return "MatchResult{" +
                "startIndex=" + startIndex +
                ", endIndex=" + endIndex +
                '}';
    }
}
