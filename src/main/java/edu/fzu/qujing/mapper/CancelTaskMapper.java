package edu.fzu.qujing.mapper;

import edu.fzu.qujing.bean.CancleTask;


public interface CancelTaskMapper {
    /**
     * 添加取消任务理由
     *
     * @param cancleTask 取消的任务信息
     */
    public void addCancelTask(CancleTask cancleTask);

}
