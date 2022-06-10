package com.example.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class SensitiveFilterUtil {
    // 节点
    class TrieNode {
        // 关键词结束标识
        private boolean KeywordEnd;
        // 子节点（可能有多个,key是下级字符，value是下级节点）
        private Map<Character, TrieNode> subNodes = new HashMap<>();
    }

    // 根节点
    private TrieNode root = new TrieNode();

    /**
     * 将敏感词添加到前缀树中
     *
     * @param keyword 敏感词
     */
    private void addKeywordToTrie(String keyword) {
        TrieNode tempNode = root;
        for (int i = 0; i < keyword.length(); i++) {  // 遍历单词中的每个字符
            char c = keyword.charAt(i);
            // 判断当前字符是否已被添加到前缀树中,如果没有则需要新建
            TrieNode subNode = tempNode.subNodes.get(c);
            if (subNode == null) {
                subNode = new TrieNode();
                tempNode.subNodes.put(c, subNode);  // 新建
            }
            // 指针指向子节点，进入下一轮循环
            tempNode = subNode;
        }
        // 设置敏感词结束标识
        tempNode.KeywordEnd = true;
    }

    /***
     * 过滤敏感词
     * @param text  待过滤的文本
     * @return 过滤敏感词后的文本
     */
    public String filterKeyword(String text) {
        if (!StringUtils.hasText(text)) {
            return null;
        }
        // 保存过滤后的文本结果
        StringBuilder result = new StringBuilder();

        // 类似于滑动窗口
        TrieNode currentNode = root;
        int begin = 0;      // 滑动窗口起始点
        int end = 0;   // 当前窗口终止点

        while (end < text.length()) {
            char c = text.charAt(end);
            // 检查下级节点
            currentNode = currentNode.subNodes.get(c);
            if (currentNode == null) {
                // 以 begin 开头的字符串不是敏感词
                result.append(text.charAt(begin));
                // 滑动窗口进入下一个位置
                end = ++begin;
                // 重新指向根节点
                currentNode = root;
            } else if (currentNode.KeywordEnd) {
                // 发现敏感词,将begin~position字符串替换掉
                result.append("***");
                // 进入下一个位置
                begin = ++end;
                // 重新指向根节点
                currentNode = root;
            } else {
                // 检查下一个字符
                end++;
            }
        }
        // 将最后一批字符计入结果
        result.append(text.substring(begin));
        return result.toString();
    }

    // 初始化树，被注解的方法，在对象加载完依赖注入（容器实例化bean后）后执行。
    @PostConstruct
    public void init() {
        // 获取敏感词文件输入流
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-word.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                // 将敏感词添加到前缀树中
                addKeywordToTrie(keyword);
            }
        } catch (IOException e) {
            log.error("加载敏感词文件失败：" + e.getMessage());
        }
    }
}
