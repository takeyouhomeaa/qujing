package edu.fzu.qujing.component.sensitivewords;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.NavigableSet;
import java.util.Objects;

public class SensitiveFilter implements Serializable {
    /**
     * 为2的n次方，考虑到敏感词大概在10k左右，
     * 这个数量应为词数的数倍，使得桶很稀疏
     * 提高不命中时hash指向null的概率，
     * 加快访问速度。
     */
    static final int DEFAULT_INITIAL_CAPACITY = 131072;

    private static SensitiveNode[] nodes = new SensitiveNode[DEFAULT_INITIAL_CAPACITY];

    /**
     * 初始化敏感词过滤器
     */
    public static  SensitiveFilter DEFAULT;

    static {
        try {
            DEFAULT = new SensitiveFilter(
                    new BufferedReader(new FileReader(new File(".\\sensitivewords\\sensi_words.txt"))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public SensitiveFilter(BufferedReader reader) {
        try{
            for(String line = reader.readLine(); line != null; line = reader.readLine()){
                put(line);
            }
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public SensitiveFilter(){

    }

    public boolean put(String word){
        // 长度小于2的不加入
        if(word == null || word.trim().length() < 2){
            return false;
        }


        StringPointer sp = new StringPointer(word.trim());
        int hash = sp.nextTwoCharHash(0);

        int mix = sp.nextTwoCharMix(0);

        int index = hash & (nodes.length - 1);

        SensitiveNode node = nodes[index];
        if(node == null){
            // 如果没有节点，则放进去一个
            node = new SensitiveNode(mix);
            // 并添加词
            node.getWords().add(sp);
            // 放入桶里
            nodes[index] = node;
        }else{
            // 如果已经有节点（1个或多个），找到正确的节点
            for(;node != null; node = node.getNext()){
                // 匹配节点
                if(node.getHeadTwoCharMix() == mix){
                    node.getWords().add(sp);
                    return true;
                }
                // 如果匹配到最后仍然不成功，则追加一个节点
                if(node.getNext() == null){
                    SensitiveNode sensitiveNode = new SensitiveNode(mix, node);
                    sensitiveNode.getWords().add(sp);
                    return true;
                }
            }
        }
        return true;
    }

    /**
     * 对句子进行敏感词过滤
     * 如果无敏感词返回输入的sentence对象，
     *
     * @param sentence 句子
     * @param replace
     * @return
     */
    public String filter(String sentence, char replace) {
        // 先转换为StringPointer
        StringPointer sp = new StringPointer(sentence);

        // 标示是否替换
        boolean replaced = false;
        int i = 0;
        //用与判断是否替换,用于跳出循环
        boolean flag = false;
        while ( i < sp.getLength() - 1){
            //每次循环开始将其初始化为false
            flag = false;
            /*
             * 移动到下一个匹配位置的步进：
             * 如果未匹配为1，如果匹配是匹配的词长度
             */
            int step = 1;

            int hash = sp.nextTwoCharHash(i);

            SensitiveNode node = nodes[hash & (nodes.length - 1)];

            if(node != null){
                int mix = sp.nextTwoCharMix(i);

                for(; node != null; node = node.getNext()){
                    if(node.getHeadTwoCharMix() == mix) {
                        /*
                         * 查出比剩余sentence长度小的最大的词。
                         */
                        NavigableSet<StringPointer> desSet = node.getWords().headSet(sp.substring(i), true);
                        if(desSet != null){
                            for(StringPointer word: desSet.descendingSet()){
                                /*
                                 * 仍然需要再判断一次，例如"色情信息哪里有？"，
                                 * 如果节点只包含"色情电影"一个词，
                                 * 仍然能够取到word为"色情电影"，但是不该匹配。
                                 */
                                if(sp.nextStartsWith(i, word)){
                                    // 匹配成功，将匹配的部分，用replace制定的内容替代
                                    sp.fill(i, i + word.getLength(), replace);
                                    // 跳过已经替代的部分
                                    step = word.getLength();
                                    // 标示有替换
                                    replaced = true;
                                    // 跳出循环（然后是while循环的下一个位置）
                                    flag = true;
                                    break;
                                }
                            }
                        }
                    }
                    if(flag){
                        break;
                    }
                }
            }
            i += step;
        }

        if(replaced){
            return sp.toString();
        }else{
            return sentence;
        }
    }

}
