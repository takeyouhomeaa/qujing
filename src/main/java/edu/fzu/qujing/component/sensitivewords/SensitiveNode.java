package edu.fzu.qujing.component.sensitivewords;

import lombok.Data;

import java.io.Serializable;
import java.util.TreeSet;

/**
 * @author
 */
@Data
public class SensitiveNode implements Serializable {

    /**
     * 头两个字符的mix，mix相同，两个字符相同
     */
    protected final Integer headTwoCharMix;

    /**
     * 所有以这两个字符开头的词表
     */
    protected final TreeSet<StringPointer> words = new TreeSet<StringPointer>();

    /**
     * 下一个节点
     */
    protected SensitiveNode next;

    public SensitiveNode(int headTwoCharMix){
        this.headTwoCharMix = headTwoCharMix;
    }

    public SensitiveNode(int headTwoCharMix, SensitiveNode parent){
        this.headTwoCharMix = headTwoCharMix;
        parent.next = this;
    }


}
