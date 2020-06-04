package edu.fzu.qujing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.fzu.qujing.bean.Task;
import edu.fzu.qujing.service.PageService;
import edu.fzu.qujing.service.TaskService;
import edu.fzu.qujing.util.PageUtil;
import edu.fzu.qujing.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static edu.fzu.qujing.util.PageUtil.PAGE_SIZE;

/**
 * @author ozg
 */
@Slf4j
@Service
public class TaskPageServiceImpl implements PageService {

    @Autowired
    TaskService taskService;

    private final static Integer PAGES = 2;


    @Override
    public void saveCache(String key,Object object,Integer timeout) {
        Task task = (Task)object;
        Long time = task.getDeadline().getTime();
        Integer expedited = task.getExpedited();
        double rate = time.doubleValue() + expedited.doubleValue();
        RedisUtil.zadd("task::" + key, task.getId(),rate);
        String key2 = "task::getDetailTask("+ task.getId() +")";
        if(RedisUtil.hasKey(key2)) {
            RedisUtil.set(key2, task);
        }
        if(timeout != null) {
            RedisUtil.expire(key2,timeout);
            RedisUtil.expire(key, timeout);
        }
    }

    @Override
    public void saveCache(String key, List<?> list, Integer timeout) {
        if (list.size() > 0) {
            for (Integer i = 0; i < list.size(); i++) {
                Task task = (Task) list.get(i);
                Long time = task.getDeadline().getTime();
                Integer expedited = task.getExpedited();
                double rate = time.doubleValue() + expedited.doubleValue();
                Integer id = task.getId();
                RedisUtil.zadd("task::" + key, id,rate);
                String key2 = "task::getDetailTask("+ id +")";
                if(RedisUtil.hasKey(key2)) {
                    RedisUtil.set(key2, task);
                }
                if(timeout != null) {
                    RedisUtil.expire(key2,timeout);
                }
            }
            if(timeout != null) {
                RedisUtil.expire(key, timeout);
            }
        }
    }

    @Override
    public List<Task> listPageDataByCache(String key, Integer pos) {
        if(pos <= 0) {
            pos = 1;
        }
        List<Task> list = new ArrayList<>();
        if(!StringUtils.isEmpty(key)) {
            Long totalCount = RedisUtil.zcard(key);
            if(totalCount > 0) {
                //计算分页
                Integer beginIndex = ((pos - 1) * PAGE_SIZE);
                Integer endIndex = (beginIndex + PAGE_SIZE - 1);


                Set<Task> sets = RedisUtil.zrevrsearrange("task::" + key, beginIndex, endIndex);
                if (sets != null && !sets.isEmpty() ) {
                    for (Task task : sets) {
                        list.add((Task) RedisUtil.get("task::getDetailTask("+ task.getId() +")"));
                    }
                }
            }
        }

        return list;
    }

    @Override
    public Integer getCount(String key,Integer pos) {
        Integer beginIndex = ((pos - 1) * PAGE_SIZE);
        Long zcount = RedisUtil.zcount(key, Integer.MIN_VALUE, Integer.MAX_VALUE);
        Integer cnt = zcount.intValue() - beginIndex;
        return cnt;
    }

    @Override
    public void delCache(String key,Integer id) {
        RedisUtil.zrem("task::" + key, id);
    }



    @Override
    public Runnable cachePreload(String studentId) {
      Runnable runnable = ()->{
          Integer count = taskService.getCount(new QueryWrapper<Task>().eq("state", 1));
          int pos = count / PAGE_SIZE;
          if(pos > PAGES ) {
              pos = PAGES;
          }
          for(Integer i = 1;i <= pos ;i++) {
              taskService.listUnacceptedTask(i,studentId);

          }

          for(Integer i = 1;i <= 5;i++) {
              pos =taskService.getCount(new QueryWrapper<Task>().eq("ttid", i));
              if(pos > PAGES ) {
                  pos = PAGES;
              }
              for(Integer j = 1;j <= pos;j++) {
                  taskService.listUnacceptedTaskByType(j,i,studentId);
              }
          }

          log.info("任务缓存预加载完毕");
      };

      return runnable;
    }



}
