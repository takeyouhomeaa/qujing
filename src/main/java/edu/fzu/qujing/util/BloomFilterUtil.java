package edu.fzu.qujing.util;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.api.R;
import com.google.gson.JsonObject;
import com.sun.org.apache.bcel.internal.generic.NEW;
import edu.fzu.qujing.component.BloomFilter;
import lombok.Data;

import javax.naming.ldap.Rdn;
import java.io.*;
import java.util.BitSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ozg
 */

public class BloomFilterUtil implements Serializable {

    private static BloomFilter bloomFilterToUser;
    private static BloomFilter bloomFilterToTask;

    static {
        bloomFilterToUser = new BloomFilter(BloomFilter.DataScale.HIGH_SCALE.getValue());
        bloomFilterToTask = new BloomFilter(BloomFilter.DataScale.HIGH_SCALE.getValue());
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



    public static boolean addIfNotExist(BloomFilter bloomFilter,String data,String key){
        boolean exist = bloomFilter.addIfNotExist(data);
        if(exist) {
            saveFilterToFile(bloomFilter,key);
        }
        return exist;
    }

    public static boolean contains(BloomFilter bloomFilter,String data) {
        return bloomFilter.contains(data);
    }

    public static void saveFilterToFile(BloomFilter bloomFilter,String key) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(".\\bloomFilter\\" + key + ".txt"))) {
            oos.writeObject(bloomFilter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static BloomFilter readFilterFromFile(String key) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(".\\bloomFilter\\" + key + ".txt"))) {
            return (BloomFilter) ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}






