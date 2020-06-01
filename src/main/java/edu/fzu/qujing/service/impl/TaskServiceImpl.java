package edu.fzu.qujing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.fzu.qujing.bean.DelayTask;
import edu.fzu.qujing.bean.Task;
import edu.fzu.qujing.bean.User;
import edu.fzu.qujing.mapper.TaskMapper;
import edu.fzu.qujing.service.CancelTaskService;
import edu.fzu.qujing.service.TaskService;
import edu.fzu.qujing.service.UserService;
import edu.fzu.qujing.util.DelayQueueUtil;
import edu.fzu.qujing.util.PageUtil;
import edu.fzu.qujing.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.fzu.qujing.util.PageUtil.PAGE_SIZE;
import static edu.fzu.qujing.util.PageUtil.PRELOAD_POS;

@Slf4j
@Service
@CacheConfig(cacheNames = "task")
@Transactional(rollbackFor = Exception.class)
public class TaskServiceImpl implements TaskService {

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    CancelTaskService cancelTaskService;

    @Autowired
    UserService userService;

    @Resource
    PageService taskPageServiceImpl;





    @Async
    public void updateCache(String key,Integer pos,String studentId){
        Integer count = taskPageServiceImpl.getCount(key, pos);
        if(count < PAGE_SIZE){
            for(Integer i = pos + 1;i < pos + 3;i++) {
                List<Task> list = listUnacceptedTask(i,studentId);
               taskPageServiceImpl.saveCache(key, list, null);
            }
        }
    }

    private List<Task> listTaskByCache(String key,Integer pos,String studentId){
        Integer tpos = pos;
        List<Task> list = taskPageServiceImpl.listPageDataByCache(key, tpos);
        if(list != null && list.size() != 0) {
            updateCache(key, pos,studentId);
            return list;
        }
        return null;
    }


    @Override
    public Integer getCount(QueryWrapper queryWrapper) {
        return taskMapper.selectCount(queryWrapper);
    }



    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
    public List<Task> listUnacceptedTask(Integer pos) {
        Page<Task> page = new Page<>(pos, PageUtil.PAGES);
        IPage<Task> taskIPage = taskMapper.listSimpleTask(page, 1);
        Map<String,Object> rs = new HashMap<>(2);
        return taskIPage.getRecords();
    }



    @Override
    @Cacheable(key = "#root.methodName + '(' + #root.args + ')'",unless = "#result == null")
    public Task getDetailTask(Integer id) {
        Task task = new Task();
        task.setId(id);
        Task taskById = taskMapper.getTaskById(task);
        return taskById;
    }



    @Override
    @Caching(
            evict = {
                    @CacheEvict(key = "'listPublish(*)'"),
            }
    )
    @CachePut(key = "'getDetailTask(' + #result.id + ')'",unless = "#result == null")
    public Task postTask(Map<String,String> map) {


        String studentId = map.get("studentId");
        Integer points = Integer.valueOf(map.get("points"));
        User user = userService.getUserPoints(studentId);
        Integer userPoints = user.getPoints();
        if(points > userPoints ) {
            return null;
        }
        user.setPoints(userPoints - points);
        userService.updatePoints(user);

        String key = "user::getUserPoints(" + user.getPhone() + ")";
        RedisUtil.set(key,user);

        Task task = new Task();
        task.setName(map.get("name"));
        task.setContent(map.get("content"));
        task.setState(1);
        task.setPoints(points);
        task.setSenderid(studentId);
        task.setTtid(Integer.valueOf(map.get("ttid")));
        Date date = new Date(System.currentTimeMillis() + Long.parseLong(map.get("ttl")) * 60000);
        task.setDeadline(date);
        task.setExpedited(Integer.valueOf(map.get("expedited")));
        taskMapper.insert(task);
        Integer id = task.getId();

        taskPageServiceImpl.delCache("listUnacceptedTask", id);

        DelayQueueUtil.addDelayTaskToCancel(new DelayTask(id,studentId,1000 * 60 * 10));
        return task;
    }

    @Override
    public Task updateState(Integer id,Integer state) {
        Task task = taskMapper.selectById(id);
        task.setState(state);
        taskMapper.updateById(task);
        return task;
    }



    @Override
    @CachePut(key = "'getDetailTask(' + #id + ')'",unless = "#result == null or #result.id == null")
    public Task acceptTask(Integer id,String studentId) {
        User user = userService.getReceiveTaskNumber(studentId);
        if(user.getReceiveTaskNumber() == 3) {
            return new Task();
        }

        Task task = taskMapper.selectById(id);
        synchronized (this) {
            if (task.getState() == 1) {
                task.setState(2);
                task.setReceiverid(studentId);
                taskMapper.updateById(task);
                userService.updateReceiveTaskNumber(studentId, 1);
                return task;
            }

        }

        return null;
    }




    @Override
    @CachePut(key = "'getDetailTask(' + #id + ')'",unless = "#result == null or #result.id == null")
    public Task cancelTaskToEmployer(Integer id,String studentId ,String content, String type) {



        cancelTaskService.save(id, content, type);
        DelayQueueUtil.removeDelayTaskToCancel(new DelayTask(id));
        return updateState(id,3);
    }




    @Caching(
            put = @CachePut(key = "'getDetailTask(' + #id + ')'",unless = "#result == null"),
            evict = {
                    @CacheEvict(key = "'listAccept( '+ #studentId + ',*)'"),
                    @CacheEvict(key = "'listPublish( '+ #studentId + ',*)'"),
                    @CacheEvict(cacheNames = "user",key = "'getUserPoints(' + #studentId + ')'")
            }
    )
    @Override
    public Task cancelTaskToEmployee(Integer id,String studentId ,String content, String type) {
        Task task = updateState(id, 4);
        User user = userService.getUserPoints(task.getSenderid());
        user.setPoints(task.getPoints() + user.getPoints());
        cancelTaskService.save(id, content, type);
        return task;
    }



    @Caching(
            put = @CachePut(key = "'getDetailTask(' + #id + ')'",unless = "#result == null"),
            evict = {
                    @CacheEvict(key = "'listAccept( '+ #studentId + ',*)'"),
                    @CacheEvict(key = "'listPublish( '+ #studentId + ',*)'")
            }
    )
    @Override
    public Task completeTaskToEmployee(Integer id,String studentId) {
        DelayQueueUtil.addDelayTaskToConfirm(new DelayTask(id,studentId ,1000 * 60 * 10));
        return updateState(id,5);
    }




    @Caching(
            put = {
                    @CachePut(key = "'getDetailTask(' + #id + ')'",unless = "#result == null"),
            },
            evict = {
                    @CacheEvict(key = "'listAccept( '+ #studentId + ',*)'"),
                    @CacheEvict(key = "'listPublish( '+ #studentId + ',*)'"),
                    @CacheEvict(cacheNames = "user",key = "'getUserPoints(' + #studentId + ')'")
            }
    )
    @Override
    public Task confirmTaskToEmployer(Integer id,String studentId) {
        Task task = updateState(id,6);
        User user = userService.getUserPoints(task.getReceiverid());
        user.setPoints(task.getPoints() + user.getPoints());

        DelayTask delayTask = new DelayTask();
        delayTask.setId(id);
        DelayQueueUtil.removeDelayTaskToConfirm(delayTask);
        return task;
    }




    @Override
    @Cacheable(key = "#root.methodName + '(' + #root.args + ')",unless = "#result == null")
    @Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
    public List<Task> listAccept(String studentId, Integer pos) {
        Page<Task> page = new Page<>(pos, PageUtil.PAGES);
        return taskMapper.listTaskByStudentId(page, null, studentId).getRecords();
    }


    @Override
    @Cacheable(key = "#root.methodName + '(' + #root.args + ')'",unless = "#result == null")
    @Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
    public List<Task> listPublish(String studentId, Integer pos) {
        Page<Task> page = new Page<>(pos, PageUtil.PAGES);
        return taskMapper.listTaskByStudentId(page,  studentId,null).getRecords();
    }

    public void taskResolve(DelayTask delayTask,Task task){
        String studentId = delayTask.getStudentId();
        String key1 = "listAccept(" + studentId + ",*)";
        String key2 = "listPublish( "+ studentId + ",*)";
        String key3 = "getDetailTask(" + delayTask.getId() + ")";
        if(RedisUtil.hasKey(key1)) {
            RedisUtil.del(key1);
        }
        if(RedisUtil.hasKey(key2)) {
            RedisUtil.del(key2);
        }
        RedisUtil.set(key3, task);
    }


    @Override
    public Runnable taskWasNotTaken() {
        Runnable runnable = ()->{
            log.info("taskWasNotTaken 线程启动");
            DelayTask delayTask = DelayQueueUtil.getDelayTaskToCancel();
            Integer id = delayTask.getId();
            Task task = updateState(id, 3);
            User user = userService.getUserPoints(task.getSenderid());
            user.setPoints(task.getPoints() + user.getPoints());

            taskResolve(delayTask,task);
            log.info("taskWasNotTaken 自动取消任务");
        };
        return runnable;
    }


    @Override
    public Runnable autoTaskConfirm() {
        Runnable runnable = ()->{
            while (true) {
                log.info("autoTaskConfirm 线程启动");
                DelayTask delayTask = DelayQueueUtil.getDelayTaskToConfirm();
                Integer id = delayTask.getId();
                Task task = updateState(id,6);
                User user = userService.getUserPoints(task.getReceiverid());
                user.setPoints(task.getPoints() + user.getPoints());

                taskResolve(delayTask,task);
                log.info("autoTaskConfirm 自动确认任务");
            }
        };
        return runnable;
    }



}
