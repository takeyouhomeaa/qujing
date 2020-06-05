package edu.fzu.qujing.component;

import lombok.Data;

import java.io.Serializable;
import java.util.BitSet;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class BloomFilter implements Serializable {



    private  int[] seeds;
    private  int size;
    private  BitSet notebook;
    private  MisjudgmentRate rate;
    private  AtomicInteger useCount = new AtomicInteger(0);
    private  Double autoClearRate;

    /**
     * 默认中等程序的误判率：MisjudgmentRate.MIDDLE 以及不自动清空数据（性能会有少许提升）
     *
     * @param dataCount 预期处理的数据规模，如预期用于处理1百万数据的查重，这里则填写1000000
     */
    public BloomFilter(int dataCount) {
        this(MisjudgmentRate.MIDDLE, dataCount, null);
    }




    /**
     * @param rate          一个枚举类型的误判率
     * @param dataCount     预期处理的数据规模，如预期用于处理1百万数据的查重，这里则填写1000000
     * @param autoClearRate 自动清空过滤器内部信息的使用比率，传null则表示不会自动清理，
     *                      当过滤器使用率达到100%时，则无论传入什么数据，都会认为在数据已经存在了
     *                      当希望过滤器使用率达到80%时自动清空重新使用，则传入0.8
     */


    public BloomFilter(MisjudgmentRate rate, int dataCount, Double autoClearRate) {
        long bitSize = rate.seeds.length * dataCount;
        if (bitSize < 0 || bitSize > Integer.MAX_VALUE) {
            throw new RuntimeException("位数太大溢出了，请降低误判率或者降低数据大小");
        }
        this.rate = rate;
        seeds = rate.seeds;
        size = (int) bitSize;
        notebook = new BitSet(size);
        this.autoClearRate = autoClearRate;
    }


    private int hash(String data, int seeds) {
        char[] value = data.toCharArray();
        int hash = 0;
        if (value.length > 0) {

            for (int i = 0; i < value.length; i++) {
                hash = i * hash + value[i];
            }
        }

        hash = hash * seeds % size;
        // 防止溢出变成负数
        return Math.abs(hash);
    }

    /**
     * 把BitSet的对应的值置1
     *
     * @param index
     */
    public void setTrue(int index) {
        useCount.incrementAndGet();
        notebook.set(index, true);
    }


    /**
     * 添加数据
     *
     * @param data
     */
    public void add(String data) {
        checkNeedClear();

        for (int i = 0; i < seeds.length; i++) {
            int index = hash(data, seeds[i]);
            setTrue(index);
        }
    }


    /**
     * 如果不存在就进行记录并返回false，如果存在了就返回true
     *
     * @param data
     * @return
     */
    public boolean addIfNotExist(String data) {
        checkNeedClear();

        int[] indexs = new int[seeds.length];
        // 先假定存在
        boolean exist = true;
        int index;

        for (int i = 0; i < seeds.length; i++) {
            indexs[i] = index = hash(data, seeds[i]);

            if (exist) {
                if (!notebook.get(index)) {
                    // 只要有一个不存在，就可以认为整个字符串都是第一次出现的
                    exist = false;
                    // 补充之前的信息
                    for (int j = 0; j <= i; j++) {
                        setTrue(indexs[j]);
                    }
                }
            } else {
                setTrue(index);
            }
        }

        return exist;

    }


    public boolean contains(String data) {
        int[] indexs = new int[seeds.length];
        // 先假定存在
        boolean exist = true;
        int index;

        for (int i = 0; i < seeds.length; i++) {
            indexs[i] = index = hash(data, seeds[i]);

            if (exist) {
                if (!notebook.get(index)) {
                    // 只要有一个不存在，就可以认为整个字符串都是第一次出现的
                    exist = false;
                    break;
                }
            }
        }

        return exist;
    }


    /**
     * 检查是否自动清空
     */
    private void checkNeedClear() {
        if (autoClearRate != null) {
            if (getUseRate() >= autoClearRate) {
                synchronized (this) {
                    if (getUseRate() >= autoClearRate) {
                        notebook.clear();
                        useCount.set(0);
                    }
                }
            }
        }
    }


    /**
     * 获取用户使用的比率
     *
     * @return
     */
    public double getUseRate() {
        return (double) useCount.intValue() / (double) size;
    }


    public MisjudgmentRate getRate() {
        return rate;
    }

    public void clear() {
        useCount.set(0);
        notebook.clear();
    }

    public enum MisjudgmentRate {
        // 这里要选取质数，能很好的降低错误率
        /**
         * 每个字符串分配4个位
         */
        VERY_SMALL(new int[]{2, 3, 5, 7}),
        /**
         * 每个字符串分配8个位
         */
        SMALL(new int[]{2, 3, 5, 7, 11, 13, 17, 19}),
        /**
         * 每个字符串分配16个位
         */
        MIDDLE(new int[]{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53}),
        /**
         * 每个字符串分配32个位
         */
        HIGH(new int[]{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97,
                101, 103, 107, 109, 113, 127, 131});

        private int[] seeds;

        private MisjudgmentRate(int[] seeds) {
            this.seeds = seeds;
        }

        public int[] getSeeds() {
            return seeds;
        }

        public void setSeeds(int[] seeds) {
            this.seeds = seeds;
        }
    }

    /**
     * 数据规模
     */
    public enum DataScale {
        /**
         * 小规模数据量
         */
        SMALL_SCALE(10000),


        /**
         * 中等规模数据量
         */
        MIDDLE_SCALE(100000),


        /**
         * 大规模数据量
         */
        HIGH_SCALE(1000000);


        DataScale(int value) {
            this.value = value;
        }

        private final int value;

        public int getValue() {
            return value;
        }
    }
}
