package com.iflove.common.algorithm.sensitiveWord;

import java.util.List;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 敏感词工厂
 */
public interface WordFactory {
    /**
     * 返回敏感词数据源
     */
    List<String> getWordList();
}
