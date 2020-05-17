package edu.fzu.qujing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.fzu.qujing.bean.DelayTask;
import edu.fzu.qujing.bean.Task;
import edu.fzu.qujing.bean.User;
import edu.fzu.qujing.mapper.TaskMapper;
import edu.fzu.qujing.service.TaskService;
import edu.fzu.qujing.util.DelayQueueUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class TaskServiceImpl implements TaskService {

    @Autowired
    TaskMapper taskMapper;
    /**
     * 查询未有人接受的任务
     *
     * @param pos
     * @param count
     * @return
     */
    @Override
    @Cacheable(cacheNames = "task",key = "#root.args")
    @Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
    public List<Task> listUnacceptedTask(Integer pos, Integer count) {
        Page<Task> page = new Page<>(pos,count);
        IPage<Task> taskIPage = taskMapper.listSimpleTask(page, 1);
        return taskIPage.getRecords();
    }

    /**
     * 根据任务id查找任务详细信息
     *
     * @param id 任务id
     * @return
     */
    @Override
    @Cacheable(cacheNames = "task",key = "#id")
    public Task getDetailTask(Integer id) {
        Task task = new Task();
        task.setId(id);
        Task taskById = taskMapper.getTaskById(task);
        return taskById;
    }

    /**
     * 发布任务
     *
     * @param task
     * @return
     */
    @Override
    public void postTask(Task task) {
        taskMapper.insert(task);
    }

    @Override
    public void updateState(Integer id,Integer state) {
        Task task = taskMapper.selectById(id);
        task.setState(state);
        taskMapper.updateById(task);
    }


    @Override
    public void acceptTask(Integer id) {
        Task task = taskMapper.selectById(id);
        task.setState(2);
        taskMapper.updateById(task);
    }



    /**
     * 雇主取消任务
     *
     * @param id      任务ID
     * @param content 理由
     * @param type    取消类型
     */
    @Override
    public void cancelTaskToEmployer(Integer id, String content, String type) {
        Task task = taskMapper.selectById(id);
        task.setState(3);
        taskMapper.updateById(task);
        //addCancelTask(id, content, type);
        DelayQueueUtil.removeDelayTaskToCancel(new DelayTask(id));
    }



    /**
     * 雇员取消任务
     *
     * @param id      任务ID
     * @param content 理由
     * @param type    取消类型
     */
    @Override
    public void cancelTaskToEmployee(Integer id, String content, String type) {
        Task task = taskMapper.selectById(id);
        task.setState(4);
        taskMapper.updateById(task);
        //addCancelTask(id, content, type);
    }



    /**
     * 雇员完成任务
     *
     * @param id 任务ID
     */
    @Override
    public void completeTaskToEmployee(Integer id) {
        Task task = taskMapper.selectById(id);
        task.setState(4);
        taskMapper.updateById(task);
    }


    /**
     * 雇主确认完成任务
     *
     * @param id 任务ID
     */
    @Override
    public void confirmTaskToEmployer(Integer id) {

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
            Task task = new Task();
            task.setId(delayTask.getId());
            task.setState(3);
            taskMapper.updateById(task);
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
                Task task = new Task();
                task.setId(delayTask.getId());
                task.setState(6);
                taskMapper.updateById(task);
                log.info("autoTaskConfirm 自动确认任务");
            }
        };
        return runnable;
    }


}
