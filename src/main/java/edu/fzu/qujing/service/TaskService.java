package edu.fzu.qujing.service;

import edu.fzu.qujing.bean.Task;
import edu.fzu.qujing.bean.User;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskService {
    /**
     * 查询未有人接受的任务
     * @return
     */
    List<Task> listUnacceptedTask(Integer pos,Integer count);

    /**
     * 根据任务id查找任务详细信息
     * @param id 任务id
     * @return
     */
    Task getDetailTask(Integer id);

    /**
     * 发布任务
     * @param task
     * @return
     */
    void postTask(Task task);

    /**
     * 修改任务的状态
     * @param id
     * @param state
     */
    void updateState(Integer id,Integer state);

    /**
     * 接受任务
     * @param id
     */
    void acceptTask(Integer id);



    /**
     * 雇主取消任务
     *
     * @param id 任务ID
     * @param content 理由
     * @param type 取消类型
     */
    public void cancelTaskToEmployer(Integer id, String content, String type);


    /**
     * 雇员取消任务
     *
     * @param id 任务ID
     * @param content 理由
     * @param type 取消类型
     */
    public void cancelTaskToEmployee(Integer id, String content, String type);


    /**
     * 雇员完成任务
     *
     * @param id 任务ID
     */
    public void completeTaskToEmployee(Integer id);


    /**
     * 雇主确认完成任务
     *
     * @param id 任务ID
     */
    public void confirmTaskToEmployer(Integer id);


    /**
     * 任务10分钟未有人接受,自动取消
     *
     * @return 异步线程
     */
    Runnable taskWasNotTaken();

    /**
     * 任务完成，自动确认
     *
     * @return 异步线程
     */
    Runnable autoTaskConfirm();
}
