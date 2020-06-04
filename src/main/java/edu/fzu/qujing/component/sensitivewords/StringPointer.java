package edu.fzu.qujing.component.sensitivewords;

import lombok.Data;

import java.io.Serializable;

/**
 * @author
 */

@Data
public class StringPointer implements Serializable, CharSequence, Comparable<StringPointer> {


    protected final char[] value;

    protected final int offset;

    protected final int length;

    protected int hash = 0;

    /**
     * 构造函数
     * 把字符串转化为字符数组
     * @param str
     */
    public StringPointer(String str){
        value = str.toCharArray();
        offset = 0;
        length = value.length;
    }

    public StringPointer(char[] value, int offset, int length){
        this.value = value;
        this.offset = offset;
        this.length = length;
    }
    /**
     * 该位置后（包含）的字符串，是否以某个词（word）开头
     *
     * @param i 从 0 到 length - 2
     * @param word 词
     * @return 是否？
     */
    public boolean nextStartsWith(int i, StringPointer word){
        // 是否长度超出
        if(word.length > length - i){
            return false;
        }
        // 从尾开始判断
        for(int c =  word.length - 1; c >= 0; c --){
            if(value[offset + i + c] != word.value[word.offset + c]){
                return false;
            }
        }
        return true;
    }

    /**
     * 计算该位置后两个字符的Mix码(包含该位置)
     * i 从 0 到 length - 2
     *
     * @param i 字符数组下标
     * @return
     */
    public Integer nextTwoCharMix(int i) {
        return (value[offset + i] << 16) | value[offset + i + 1];
    }

    /**
     * 计算该位置后（包含）2个字符的hash值
     *
     * i 从 0 到 length - 2
     * @param i
     * @return
     */
    public Integer nextTwoCharHash(int i){
        return 31 * value[offset + i] + value[offset + i + 1];
    }

    /**
     * 填充（替换）
     *
     * @param begin 从此位置开始（含）
     * @param end 到此位置结束（不含）
     * @param fillWith 以此字符填充（替换）
     */
    public void fill(int begin, int end, char fillWith){
        for(int i = begin; i < end; i ++){
            value[offset + i] = fillWith;
        }
    }

    @Override
    public int length(){
        return length;
    }

    @Override
    public char charAt(int i){
        return value[offset + i];
    }

    public StringPointer substring(int begin){
        return new StringPointer(value, offset + begin, length - begin);
    }

    public StringPointer substring(int begin, int end){
        return new StringPointer(value, offset + begin, end - begin);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return substring(start, end);
    }

    @Override
    public int hashCode() {
        int h = hash;
        if (h == 0 && length > 0) {
            for (int i = 0; i < length; i++) {
                h = 31 * h + value[offset + i];
            }
            hash = h;
        }
        return h;
    }

    @Override
    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof StringPointer) {
            StringPointer that = (StringPointer)anObject;
            if (length == that.length) {
                char v1[] = this.value;
                char v2[] = that.value;
                for(int i = 0; i < this.length; i ++){
                    if(v1[this.offset + i] != v2[that.offset + i]){
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }


    @Override
    public int compareTo(StringPointer o) {
        int len1 = this.length;
        int len2 = o.length;
        int lim = Math.min(len1, len2);
        char v1[] = this.value;
        char v2[] = o.value;

        int k = 0;
        while (k < lim ) {
            char c1 = v1[this.offset + k];
            char c2 = v2[o.offset + k];
            if (c1 != c2) {
                return c1 - c2;
            }
            k++;
        }

        return len1 - len2;
    }
}
