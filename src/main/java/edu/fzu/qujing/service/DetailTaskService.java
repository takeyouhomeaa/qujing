package edu.fzu.qujing.service;

import edu.fzu.qujing.bean.DetailTask;
import edu.fzu.qujing.bean.Task;

import java.util.List;

public interface DetailTaskService {
    /**
     * 查询未有人接受的任务
     * @param pos
     * @param studentId
     * @return
     */
    List<DetailTask> listUnacceptedTask(Integer pos, String studentId);


    /**
     * 以类型查询未有人接受的任务
     * @param pos
     * @param type
     * @param studentId
     * @return
     */
    List<DetailTask> listUnacceptedTaskByType(Integer pos,Integer type,String studentId);




    /**
     * 按照学号查找已接受的任务(完成)
     *
     * @param studentId
     * @param pos
     * @return
     */
    List<DetailTask> listAccept(String studentId, Integer pos);



    /**
     * 按照学号查找已发布的任务(确认)
     *
     * @param studentId
     * @param pos
     * @return
     */
    List<DetailTask> listPublish(String studentId,Integer pos);



    /**
     * 根据任务id查找任务详细信息
     *
     * @param id 任务id
     * @return
     */
    DetailTask getDetailTask(Integer id);
}
