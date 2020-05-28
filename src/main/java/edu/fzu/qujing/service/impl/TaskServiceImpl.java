package edu.fzu.qujing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.fzu.qujing.bean.DelayTask;
import edu.fzu.qujing.bean.Task;
import edu.fzu.qujing.bean.User;
import edu.fzu.qujing.mapper.TaskMapper;
import edu.fzu.qujing.service.CancelTaskService;
import edu.fzu.qujing.service.TaskService;
import edu.fzu.qujing.util.AuthorityUtil;
import edu.fzu.qujing.util.DelayQueueUtil;
import edu.fzu.qujing.util.PageUtil;
import edu.fzu.qujing.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@CacheConfig(cacheNames = "task")
@Transactional(rollbackFor = Exception.class)
public class TaskServiceImpl implements TaskService {

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    CancelTaskService cancelTaskService;

    /**
     * 查询未有人接受的任务
     *
     * @param pos
     * @return
     */
    @Override
    @Cacheable(key = "#root.methodName + '(' + #root.args + ')'",unless = "#result == null")
    @Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
    public List<Task> listUnacceptedTask(Integer pos) {
        Page<Task> page = new Page<>(pos, PageUtil.PAGES);
        IPage<Task> taskIPage = taskMapper.listSimpleTask(page, 1);
        Map<String,Object> rs = new HashMap<>(2);
        return taskIPage.getRecords();
    }

    /**
     * 根据任务id查找任务详细信息
     *
     * @param id 任务id
     * @return
     */
    @Override
    @Cacheable(key = "#root.methodName + '(' + #root.args + ')'",unless = "#result == null")
    public Task getDetailTask(Integer id) {
        Task task = new Task();
        task.setId(id);
        Task taskById = taskMapper.getTaskById(task);
        return taskById;
    }

    /**
     * 发布任务
     *
     * @param map
     * @return
     */
    @Override
    @Caching(
            put = @CachePut(key = "'getDetailTask(' + #result.id + ')'",unless = "#result == null"),
            evict = {
                    @CacheEvict(key = "'listUnacceptedTask(*)'"),
                    @CacheEvict(key = "'listPublish(*)'"),
                    @CacheEvict(cacheNames = "user",key = "'getUserPoints(' + #result.senderid + ')'")
            }
    )
    public Task postTask(Map<String,String> map) {
        Task task = new Task();
        task.setName(map.get("name"));
        task.setContent(map.get("content"));
        task.setState(1);
        task.setPoints(Integer.valueOf(map.get("points")));
        task.setSenderid(map.get("studentId"));
        task.setTtid(Integer.valueOf(map.get("ttid")));
        Date date = new Date(System.currentTimeMillis() + Long.parseLong(map.get("ttl")) * 60000);
        task.setDeadline(date);
        task.setExpedited(Integer.valueOf(map.get("expedited")));
        taskMapper.insert(task);
        Integer id = task.getId();
        DelayQueueUtil.addDelayTaskToCancel(new DelayTask(id,AuthorityUtil.getPrincipal(),1000 * 60 * 10));
        return task;
    }

    @Override
    public Task updateState(Integer id,Integer state) {
        Task task = taskMapper.selectById(id);
        task.setState(state);
        taskMapper.updateById(task);
        return task;
    }


    /**
     * 接受任务
     *
     * @param id
     * @param studentId
     * @return
     */
    @Caching(
            put = @CachePut(key = "'getDetailTask(' + #id + ')'",unless = "#result == null"),
            evict = {
                    @CacheEvict(key = "'listUnacceptedTask(*)'"),
                    @CacheEvict(key = "'listAccept(*)'")
            }
    )
    @Override
    public Task acceptTask(Integer id,String studentId) {
        Task task = taskMapper.selectById(id);
        if(task.getState() == 1){
            task.setState(2);
            task.setReceiverid(studentId);
            taskMapper.updateById(task);
            return task;
        }
        return null;
    }



    /**
     * 雇主取消任务
     *
     * @param id      任务ID
     * @param content 理由
     * @param type    取消类型
     */
    @Caching(
            put = @CachePut(key = "'getDetailTask(' + #id + ')'",unless = "#result == null"),
            evict = {
                    @CacheEvict(key = "'listAccept( '+ #studentId + ',*)'"),
                    @CacheEvict(key = "'listPublish( '+ #studentId + ',*)'"),
                    @CacheEvict(cacheNames = "user",key = "'getUserPoints(' + #studentId + ')'")
            }
    )
    @Override
    public Task cancelTaskToEmployer(Integer id,String studentId ,String content, String type) {

        cancelTaskService.save(id, content, type);
        DelayQueueUtil.removeDelayTaskToCancel(new DelayTask(id));
        return updateState(id,3);
    }



    /**
     * 雇员取消任务
     *
     * @param id      任务ID
     * @param content 理由
     * @param type    取消类型
     */
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
        cancelTaskService.save(id, content, type);
        return updateState(id,4);
    }



    /**
     * 雇员完成任务
     *
     * @param id 任务ID
     */
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


    /**
     * 雇主确认完成任务
     *
     * @param id 任务ID
     */
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
        DelayTask delayTask = new DelayTask();
        delayTask.setId(id);
        DelayQueueUtil.removeDelayTaskToConfirm(delayTask);
        return updateState(id,6);
    }



    /**
     * 按照学号查找已接受的任务
     *
     * @param studentId
     * @param pos
     * @return
     */
    @Override
    @Cacheable(key = "#root.methodName + '(' + #root.args + ')",unless = "#result == null")
    @Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
    public List<Task> listAccept(String studentId, Integer pos) {
        Page<Task> page = new Page<>(pos, PageUtil.PAGES);
        return taskMapper.listTaskByStudentId(page, null, studentId).getRecords();
    }

    /**
     * 按照学号查找已发布的任务
     *
     * @param studentId
     * @param pos
     * @return
     */
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

    /**
     * 任务10分钟未有人接受,自动取消
     *
     * @return 异步线程
     */
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

    /**
     * 任务完成，自动确认
     *
     * @return 异步线程
     */
    @Override
    public Runnable autoTaskConfirm() {
        Runnable runnable = ()->{
            while (true) {
                log.info("autoTaskConfirm 线程启动");
                DelayTask delayTask = DelayQueueUtil.getDelayTaskToConfirm();
                Integer id = delayTask.getId();
                Task task = updateState(id,6);
                taskResolve(delayTask,task);
                log.info("autoTaskConfirm 自动确认任务");
            }
        };
        return runnable;
    }

}
