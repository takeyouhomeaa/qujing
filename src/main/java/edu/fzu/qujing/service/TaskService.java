package edu.fzu.qujing.service;

import edu.fzu.qujing.bean.Task;
import edu.fzu.qujing.bean.User;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface TaskService {


    /**
     * 查询未有人接受的任务
     * @param pos
     * @return
     */
    List<Task> listUnacceptedTask(Integer pos);

    /**
     * 按照学号查找已接受的任务
     *
     * @param studentId
     * @param pos
     * @return
     */
    List<Task> listAccept(String studentId, Integer pos);



    /**
     * 按照学号查找已发布的任务
     *
     * @param studentId
     * @param pos
     * @return
     */
    List<Task> listPublish(String studentId,Integer pos);



    /**
     * 根据任务id查找任务详细信息
     * @param id 任务id
     * @return
     */
    Task getDetailTask(Integer id);

    /**
     * 发布任务
     * @param map
     * @return
     */
    Task postTask(Map<String,String> map);

    /**
     * 修改任务的状态
     * @param id
     * @param state
     * @return
     */
    Task updateState(Integer id,Integer state);

    /**
     * 接受任务
     * @param id
     * @param studentId
     * @return
     */
    Task acceptTask(Integer id,String studentId);



    /**
     * 雇主取消任务
     *
     * @param id 任务ID
     * @param studentId
     * @param content 理由
     * @param type 取消类型
     * @return
     */
    public Task cancelTaskToEmployer(Integer id,String studentId ,String content, String type);


    /**
     * 雇员取消任务
     *
     * @param id 任务ID
     * @param studentId
     * @param content 理由
     * @param type 取消类型
     * @return
     */
    public Task cancelTaskToEmployee(Integer id, String studentId ,String content, String type);


    /**
     * 雇员完成任务
     *
     * @param id 任务ID
     * @param studentId
     * @return
     */
    public Task completeTaskToEmployee(Integer id,String studentId);


    /**
     * 雇主确认完成任务
     *
     * @param id 任务ID
     * @param studentId
     * @return
     */
    public Task confirmTaskToEmployer(Integer id,String studentId);


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
