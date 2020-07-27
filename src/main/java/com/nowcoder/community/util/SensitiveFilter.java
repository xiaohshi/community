package com.nowcoder.community.util;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SensitiveFilter {

    //替换符
    private static final String REPLACEMENT = "***";

    //根节点
    private TrieNode root = new TrieNode();

    @PostConstruct
    public void init() {
        try(
                InputStream resource = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(resource))
        ) {
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                // 添加到前缀树
                this.addKeyWord(keyword);
            }
        } catch (IOException e) {
            log.error("加载敏感词失败");
        }
    }

    /**
     * 过滤敏感词 参数是待过滤文本
     * @return 过滤后文本
     */
    public String filter(String text) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }

        //指针1，指向树的节点
        TrieNode tempNode = root;
        //指针2
        int begin = 0;
        //指针3, 用它来判断是否结束
        int posision = 0;

        //结果
        StringBuilder result = new StringBuilder();

        while (posision < text.length()) {
            char c = text.charAt(posision);

            //跳过符号，比如：*开*票*，这个需要忽略掉*
            if (idSymbol(c)) {
                //若指针1处于根节点，将此符号计入结果，让指针2向下走一步
                if (tempNode == root) {
                    result.append(c);
                    begin ++;
                }
                //无论符号在开头还是中间，指针3都向下走一步
                posision ++;
                continue;
            }

            // 检查下级节点
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null) {
                //以给定开头的字符串不是敏感词
                result.append(text.charAt(begin));
                //进入下一个位置
                posision = ++ begin;
                //重新指向根节点
                tempNode = root;
            } else if (tempNode.isKeyWordEnd()) {
                //发现敏感词，将begin-position字符串替换掉
                result.append(REPLACEMENT);
                //进入下一个位置
                begin = ++posision;
                //重新指向根节点
                tempNode = root;
            } else {
                //检查下一个字符
                posision ++;
            }
        }
        //将最后一批字符计入结果
        result.append(text.substring(begin));
        return result.toString();
    }

    //判断是否是符号
    private boolean idSymbol(Character c) {
        //0x2E80-0x9FFF 是东亚文字范围
        return CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    //将一个敏感词添加到前缀树中
    private void addKeyWord(String keyword) {
        TrieNode tempNode = root;
        for (int i = 0; i < keyword.length(); i ++) {
            char c = keyword.charAt(i);
            TrieNode node = tempNode.getSubNode(c);
            if (node == null) {
                //初始化子节点
                node = new TrieNode();
                tempNode.addSubNode(c, node);
            }
            //指向子节点，进入下一轮循环
            tempNode = node;

            //设置结束的标志
            if (i == keyword.length() -1 ) {
                tempNode.setKeyWordEnd(true);
            }
        }
    }

    //前缀树
    private class TrieNode{
        @Getter
        @Setter
        //关键词结束标志
        private boolean isKeyWordEnd = false;

        //子节点，key是夏季字符，value是下级节点
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        //添加子节点
        private void addSubNode(Character character, TrieNode node) {
            subNodes.put(character, node);
        }

        //获取子节点
        private TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }
    }
}