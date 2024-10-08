package com.iflove.sensitive.algorithm.sensitiveWord.ac;

import java.util.*;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote AC自动机
 */
public class ACTrie {
    // 根节点
    ACTrieNode root;

    private final static String skipChars = " !*-+_=,，.@;:；：。、？?（）()【】[]《》<>“”\"‘’"; // 遇到这些字符就会跳过
    private final static Set<Character> skipSet = new HashSet<>(); // 遇到这些字符就会跳过

    static {
        for (char c : skipChars.toCharArray()) {
            skipSet.add(c);
        }
    }

    /**
     * 初始化AC自动机
     * 先建立前缀树，再初始化fail指针
     * @param words 目标串
     */
    public ACTrie(List<String> words) {
        root = new ACTrieNode();
        words.forEach(this::addWord);
        initFailover();
    }

    /**
     * 构建前缀树
     * @param word 目标串
     */
    private void addWord(String word) {
        ACTrieNode walkNode = root;
        char[] chars = word.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            // 如果是大写字母, 转换为小写
            c = tolower(c);
            // 特殊字符，跳过
            if (skip(c)) continue;
            walkNode.addChildrenIfAbsent(c);
            walkNode = walkNode.childOf(c);
            walkNode.setDepth(i + 1);
        }
        walkNode.setLeaf(true);
    }

    /**
     * 初始化fail指针
     * BFS遍历前缀树
     */
    private void initFailover() {
        Queue<ACTrieNode> queue = new LinkedList<>();
        // 第一层的 fail 指针指向 root
        Map<Character, ACTrieNode> children = root.getChildren();
        for (ACTrieNode child : children.values()) {
            child.setFailover(root);
            queue.offer(child);
        }
        // BFS遍历剩余节点，构建 fail 指针
        while (!queue.isEmpty()) {
            ACTrieNode parentNode = queue.poll();
            for (Map.Entry<Character, ACTrieNode> entry : parentNode.getChildren().entrySet()) {
                ACTrieNode childNode = entry.getValue();
                ACTrieNode failover = parentNode.getFailover();
                // 在树中找到以childNode为结尾的字符串的最长前缀匹配，failover指向了这个最长前缀匹配的父节点
                while (failover != null && !failover.hasChild(entry.getKey())) {
                    failover = failover.getFailover();
                }
                // fail 指针指向了 root 节点
                if (failover == null) {
                    childNode.setFailover(root);
                } else {
                    // 更新 fail 指针
                    childNode.setFailover(failover.childOf(entry.getKey()));
                }
                queue.offer(childNode);
            }
        }
    }

    /**
     * 查询句子中包含的敏感词的起始位置和结束位置
     * @param text 文本
     * @return 匹配结果集合
     */
    public List<MatchResult> matches(String text) {
        List<MatchResult> result = new ArrayList<>();
        ACTrieNode walkNode = root;
        int startIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            // 特殊字符，跳过
            if (skip(c)) continue;
            // 如果是大写字母, 转换为小写
            c = tolower(c);
            // 当前节点无法匹配，根据 fail 指针寻找下一个节点
            while (!walkNode.hasChild(c) && walkNode.getFailover() != null) {
                walkNode = walkNode.getFailover();
            }
            // 当前节点存在可以匹配的子节点，更新节点为子节点
            if (walkNode.hasChild(c)) {
                walkNode = walkNode.childOf(c);
                // 记录敏感词开始位置
                if (startIndex == -1) {
                    startIndex = i;
                }
                // 检索到了敏感词
                if (walkNode.isLeaf()) {
                    result.add(new MatchResult(startIndex, i + 1));
                    // 模式串回退到最长可匹配前缀位置并开启新一轮的匹配
                    // 这种回退方式将一个不漏的匹配到所有的敏感词，匹配结果的区间可能会有重叠的部分
                    walkNode = walkNode.getFailover();
                    startIndex = -1;
                }
            }
        }
        return result;
    }

    /**
     * 判断是否需要跳过当前字符
     *
     * @param c 待检测字符
     * @return true: 需要跳过, false: 不需要跳过
     */
    private boolean skip(char c) {
        return skipSet.contains(c);
    }

    /**
     * 如果是大写，返回小写
     */
    private char tolower(char c) {
        if (c >= 'A' && c <= 'Z') {
            c += 32;
        }
        return c;
    }
}
