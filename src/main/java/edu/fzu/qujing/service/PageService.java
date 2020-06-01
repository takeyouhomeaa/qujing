package edu.fzu.qujing.service;

import edu.fzu.qujing.bean.Task;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PageService {

    /**
     * 保存缓存
     *
     * @param key
     * @param id
     * @param timeout
     */
    void saveCache(String key, Integer id, final Integer timeout);

    /**
     * 保存缓存
     *
     * @param key
     * @param list
     * @param timeout
     */
    void saveCache(String key, List<Task> list, final Integer timeout);


    /**
     * 获得缓存列表
     *
     * @param key
     * @param pos
     * @return
     */
    List<Task> listPageDataByCache(String key,Integer pos);


    /**
     * 获得剩下的记录数量
     *
     * @param key
     * @param pos
     * @return
     */
    Integer getCount(String key,Integer pos);


    /**
     * 删除记录
     *
     * @param key
     * @param id
     */
    void delCache(String key,Integer id);


}
