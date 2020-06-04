package edu.fzu.qujing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.fzu.qujing.bean.DelayTask;
import edu.fzu.qujing.bean.DetailTask;
import edu.fzu.qujing.bean.Task;
import edu.fzu.qujing.bean.User;
import edu.fzu.qujing.mapper.TaskMapper;
import edu.fzu.qujing.service.CancelTaskService;
import edu.fzu.qujing.service.PageService;
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

import javax.annotation.Resource;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
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
    public Integer getCountToUnacceptedTask(String studentId) {
        Integer temp1 = getCount(new QueryWrapper<Task>()
                .eq("state", 1));
        Integer temp2 = getCount((new QueryWrapper<Task>()
                .eq("state", 1)
                .eq("studentId", studentId)));
        return (temp1 - temp2);
    }


    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
    public List<Task> listUnacceptedTask(Integer pos,String studentId) {

        String key = "listUnacceptedTask";
        List<Task> list = listTaskByCache(key, pos, studentId);
        if(list != null){
            return list;
        }
        Page<Task> page = new Page<>(pos, PAGE_SIZE);
        IPage<Task> taskIPage = taskMapper.selectPage(page, new QueryWrapper<Task>()
                .eq("state", 1)
                .ne("senderid", studentId)
                .orderByDesc("expedited")
                .orderByAsc("deadline"));
        List<Task> records = taskIPage.getRecords();

        taskPageServiceImpl.saveCache(key, records, null);
        return records;
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
        User user = userService.getUserInfo(studentId);
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
            String key = "listAccept::" + studentId;
            taskPageServiceImpl.saveCache(key, task, null);
        }

        return null;
    }


    private void recoveryPoints(Integer id, String content, Integer type, Task task) {
        User user = userService.getUserInfo(task.getSenderid());
        user.setPoints(task.getPoints() + user.getPoints());

        String key = "user::getUserPoints(" +user.getStudentId() + ")";
        if(RedisUtil.hasKey(key)) {
            RedisUtil.set(key, user);
        }

        cancelTaskService.save(id, content, type);
    }



    @Override
    @CachePut(key = "'getDetailTask(' + #id + ')'",unless = "#result == null or #result.id == null")
    public Task cancelTaskToEmployer(Integer id,String studentId ,String content, String type) {
        Task task = updateState(id, 3);
        recoveryPoints(id, content, Integer.valueOf(type), task);
        DelayQueueUtil.removeDelayTaskToCancel(new DelayTask(id));
        return task;
    }



    @Override
    @CachePut(key = "'getDetailTask(' + #id + ')'",unless = "#result == null or #result.id == null")
    public Task cancelTaskToEmployee(Integer id,String studentId ,String content, String type) {
        Task task = updateState(id, 4);
        recoveryPoints(id, content, Integer.valueOf(type), task);
        return task;
    }



    @Override
    @CachePut(key = "'getDetailTask(' + #id + ')'",unless = "#result == null or #result.id == null")
    public Task completeTaskToEmployee(Integer id,String studentId) {
        DelayQueueUtil.addDelayTaskToConfirm(new DelayTask(id,studentId ,1000 * 60 * 10));
        return updateState(id,5);
    }





    @Override
    @CachePut(key = "'getDetailTask(' + #id + ')'",unless = "#result == null")
    public Task confirmTaskToEmployer(Integer id,String studentId) {
        Task task = updateState(id,6);
        User user = userService.getUserInfo(task.getReceiverid());
        user.setPoints(task.getPoints() + user.getPoints());

        String key = "user::getUserPoints(" +user.getStudentId() + ")";
        if(RedisUtil.hasKey(key)) {
            RedisUtil.set(key, user);
        }

        DelayTask delayTask = new DelayTask();
        delayTask.setId(id);
        DelayQueueUtil.removeDelayTaskToConfirm(delayTask);
        return task;
    }




    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
    public List<Task> listAccept(String studentId, Integer pos) {

        String key = "listAccept::" + studentId;
        List<Task> list = listTaskByCache(key, pos, studentId);
        if(list != null){
            return list;
        }

        Page<Task> page = new Page<>(pos, PAGE_SIZE);
        List<Task> records = taskMapper.selectPage(page, new QueryWrapper<Task>()
                .eq("receiverid", studentId)
                .orderByAsc("time")).getRecords();

        taskPageServiceImpl.saveCache(key, records, null);
        return records;
    }



    @Override
    public List<Task> listUnacceptedTaskByType(Integer pos, Integer type,String studentId) {
        String key = "listUnacceptedTaskByType::" + type;
        List<Task> list = listTaskByCache(key, pos, studentId);
        if(list != null){
            return list;
        }
        Page<Task> page = new Page<>(pos, PAGE_SIZE);
        List<Task> records = taskMapper.selectPage(page, new QueryWrapper<Task>()
                .eq("state", 1)
                .eq("ttid", type)
                .ne("senderid", studentId)
                .orderByDesc("expedited")
                .orderByAsc("deadline")).getRecords();

        taskPageServiceImpl.saveCache(key, records, null);
        return records;
    }


    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
    public List<Task> listPublish(String studentId, Integer pos) {
        String key = "listPublish::" + studentId;
        List<Task> list = listTaskByCache(key, pos, studentId);
        if(list != null){
            return list;
        }

        Page<Task> page = new Page<>(pos, PAGE_SIZE);
        List<Task> records = taskMapper.selectPage(page, new QueryWrapper<Task>()
                .eq("senderid", studentId)
                .orderByAsc("time")).getRecords();

        taskPageServiceImpl.saveCache(key, records, null);
        return records;
    }



    public void taskResolve(DelayTask delayTask,Task task){
        String key = "getDetailTask(" + delayTask.getId() + ")";

        if(RedisUtil.hasKey(key)) {
            RedisUtil.set(key, task);
        }

        recoveryPoints(task.getId(),"太久无人接单",2,task);

    }


    @Override
    public Runnable taskWasNotTaken() {
        Runnable runnable = ()->{
            log.info("taskWasNotTaken 线程启动");
            DelayTask delayTask = DelayQueueUtil.getDelayTaskToCancel();
            Integer id = delayTask.getId();
            Task task = updateState(id, 3);

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
                User user = userService.getUserInfo(task.getReceiverid());
                user.setPoints(task.getPoints() + user.getPoints());

                taskResolve(delayTask,task);
                log.info("autoTaskConfirm 自动确认任务");
            }
        };
        return runnable;
    }




}
