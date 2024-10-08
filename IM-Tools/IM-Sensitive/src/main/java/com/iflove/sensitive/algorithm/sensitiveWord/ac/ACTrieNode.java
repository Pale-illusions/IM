package com.iflove.sensitive.algorithm.sensitiveWord.ac;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote AC自动机节点
 */
@Getter
@Setter
public class ACTrieNode {

    // 子节点
    private Map<Character, ACTrieNode> children = new HashMap<>();

    // fail指针
    private ACTrieNode failover = null;

    // 深度
    private int depth;

    // 是否是终止节点
    private boolean isLeaf = false;

    public void addChildrenIfAbsent(char c) {
        children.computeIfAbsent(c, k -> new ACTrieNode());
    }

    public ACTrieNode childOf(char c) {
        return children.get(c);
    }

    public boolean hasChild(char c) {
        return children.containsKey(c);
    }

    @Override
    public String toString() {
        return "ACTrieNode{" +
                "failover=" + failover +
                ", depth=" + depth +
                ", isLeaf=" + isLeaf +
                '}';
    }
}
