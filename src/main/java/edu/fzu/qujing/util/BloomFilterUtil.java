package edu.fzu.qujing.util;

import com.baomidou.mybatisplus.extension.api.R;
import com.sun.org.apache.bcel.internal.generic.NEW;
import edu.fzu.qujing.component.BloomFilter;
import lombok.Data;

import javax.naming.ldap.Rdn;
import java.io.Serializable;
import java.util.BitSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ozg
 */

public class BloomFilterUtil implements Serializable {

    private static BloomFilter bloomFilterToUser;
    private static BloomFilter bloomFilterToTask;
    private static BloomFilter bloomFilterToExpenses;
    private static BloomFilter bloomFilterToRecharge;

    static {
        bloomFilterToUser = new BloomFilter(BloomFilter.DataScale.MIDDLE_SCALE.getValue());
        bloomFilterToTask = new BloomFilter(BloomFilter.DataScale.HIGH_SCALE.getValue());
        bloomFilterToExpenses = new BloomFilter(BloomFilter.DataScale.HIGH_SCALE.getValue());
        bloomFilterToRecharge = new BloomFilter(BloomFilter.DataScale.HIGH_SCALE.getValue());
    }

    public static void setBloomFilterToUser(BloomFilter bf){
        bloomFilterToUser = bf;
    }

    public static void setBloomFilterToTask(BloomFilter bf){
        bloomFilterToTask = bf;
    }




    public static BloomFilter getBloomFilterToUser(){
        return bloomFilterToUser;
    }

    public static BloomFilter getBloomFilterToTask(){
        return bloomFilterToTask;
    }



    public static boolean addIfNotExist(BloomFilter bloomFilter,String data){
        boolean exist = bloomFilter.addIfNotExist(data);
        if(exist) {
            saveFilterToCache();
        }
        return exist;
    }

    public static boolean contains(BloomFilter bloomFilter,String data) {
        return bloomFilter.contains(data);
    }

    public static void saveFilterToCache() {
        RedisUtil.set("bloomFilterToUser", bloomFilterToUser);
        RedisUtil.set("bloomFilterToTask", bloomFilterToTask);
    }

    public static BloomFilter readFilterFromCache(String key) {
        if(RedisUtil.hasKey(key)) {
            Object bloomFilter = RedisUtil.get(key);
            return (BloomFilter)bloomFilter;
        }
        return null;
    }

}






