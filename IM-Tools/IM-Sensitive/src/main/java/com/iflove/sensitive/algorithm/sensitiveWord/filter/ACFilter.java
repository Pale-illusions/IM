package com.iflove.sensitive.algorithm.sensitiveWord.filter;

import com.iflove.sensitive.algorithm.sensitiveWord.ac.ACTrie;
import com.iflove.sensitive.algorithm.sensitiveWord.ac.MatchResult;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote AC自动机过滤器
 */
public class ACFilter implements SensitiveWordFilter {

    private final static char replace = '*'; // 替代字符

    private static ACTrie trie = null;

    /**
     * 存在敏感词
     * @param text 文本
     * @return boolean
     */
    @Override
    public boolean hasSensitiveWord(String text) {
        if (StringUtils.isBlank(text)) return false;
        return !Objects.equals(filter(text), text);
    }

    /**
     * 敏感词过滤
     * @param text 文本
     * @return 过滤后的文本
     */
    @Override
    public String filter(String text) {
        if (StringUtils.isBlank(text)) return text;
        List<MatchResult> results = trie.matches(text);
        StringBuilder result = new StringBuilder(text);
        // matchResults是按照startIndex排序的，因此可以通过不断更新endIndex最大值的方式算出尚未被替代部分
        int endIndex = 0;
        for (MatchResult matchResult : results) {
            endIndex = Math.max(endIndex, matchResult.getEndIndex());
            replaceBetween(result, matchResult.getStartIndex(), endIndex);
        }
        return result.toString();
    }

    /**
     * 替换敏感词
     */
    private static void replaceBetween(StringBuilder builder, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            builder.setCharAt(i, replace);
        }
    }

    /**
     * 加载敏感词列表
     * @param words 敏感词数组
     */
    @Override
    public void loadWord(List<String> words) {
        if (words == null) return;
        trie = new ACTrie(words);
    }
}
