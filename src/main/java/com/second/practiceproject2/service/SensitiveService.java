package com.second.practiceproject2.service;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
//初始化service时implements InitializingBean
public class SensitiveService implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);

    //默认敏感词替换符
    private static final String DEFAULT_REPLACEMENT = "敏感词";

    // TrieNode前缀树结点，建class建树
    private class TrieNode {
        // true 表示是敏感词的词尾 ； false 继续
        private boolean end = false;
         //key下一个字符，value是对应的节点，用map存敏感词的结点（不知道有几个节点）
        private Map<Character, TrieNode> subNodes = new HashMap<>();
         //向指定位置添加节点树
        void addSubNode(Character key, TrieNode node) {
            subNodes.put(key, node);
        }
         //获取当前节点的下个节点
        TrieNode getSubNode(Character key) {
            return subNodes.get(key);
        }
        //判断是否结尾
        boolean isKeywordEnd() {
            return end;
        }
        void setKeywordEnd(boolean end) {
            this.end = end;
        }
        public int getSubNodeCount() {
            return subNodes.size();
        }

    }


     //根节点
    private TrieNode rootNode = new TrieNode();

     //判断是否是一个符号（防敏感词中间插入符号）
    private boolean isSymbol(char c) {
        int ic = (int) c;
        // 0x2E80-0x9FFF 东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }


    //过滤敏感词，核心
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return text;
        }

        String replacement = DEFAULT_REPLACEMENT;//设置替代符
        StringBuilder result = new StringBuilder();//存过滤后的结果

        TrieNode tempNode = rootNode;//指向根节点的指针，1号指针
        int position = 0; // 当前比较的位置，2号指针
        int begin = 0; // 回滚数，3号指针

        //大循环while就是控制2号指针不走到头
        while (position < text.length()) {
            char c = text.charAt(position);//取出当前值
            //判断是否有符号干扰
            if (isSymbol(c)) {
                if (tempNode == rootNode) {
                    result.append(c);
                    ++begin;
                }
                ++position;//跳过符号
                continue;
            }
            tempNode = tempNode.getSubNode(c);//取当前节点的下个节点
            // 下一个不匹配，当前位置的匹配结束
            if (tempNode == null) {
                result.append(text.charAt(begin));//没有敏感词，直接append进去
                position = begin + 1;//2向下走，1对齐2，3归位跟节点
                begin = position;
                tempNode = rootNode;
            } else if (tempNode.isKeywordEnd()) {
                //和结尾相符，为敏感词，从begin到position的位置用replacement打码替换掉
                result.append(replacement);//此句注释则直接删除敏感词不打码
                position = position + 1;//2接着敏感词尾向下走，1对齐2，3归位跟节点
                begin = position;
                tempNode = rootNode;
            } else {
                ++position;
            }
        }
        result.append(text.substring(begin));//最后一个

        return result.toString();
    }

    //把词加进树中，判断根节点有没有abc，没a把a加进去
    private void addWord(String lineTxt) {
        TrieNode tempNode = rootNode;//当前节点指向根
        for (int i = 0; i < lineTxt.length(); ++i) {
            Character c = lineTxt.charAt(i);
            // 过滤敏感词文件中敏感词的符号
            if (isSymbol(c)) {
                continue;
            }
            TrieNode node = tempNode.getSubNode(c);
            if (node == null) { // 没节点，新建
                node = new TrieNode();
                tempNode.addSubNode(c, node);
            }
            tempNode = node;
            if (i == lineTxt.length() - 1) {
                // 词尾设置结束标志
                tempNode.setKeywordEnd(true);
            }
        }
    }

    @Override
    //读取文本
    public void afterPropertiesSet() throws Exception {
        rootNode = new TrieNode();
        try {
            //读取文件的固定用法
            InputStream is = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("SensitiveWords.txt");
            //逐行读取
            InputStreamReader read = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            //while控制文本一行行读
            while ((lineTxt = bufferedReader.readLine()) != null) {
                lineTxt = lineTxt.trim();//trim()去前后空格
                addWord(lineTxt);
            }
            read.close();
        } catch (Exception e) {
            logger.error("读取敏感词文件失败" + e.getMessage());
        }
    }

    public static void main(String[] argv) {
        SensitiveService s = new SensitiveService();
        s.addWord("色情");
        s.addWord("好色");
        System.out.print(s.filter("你好X色**情XX"));
    }
}
