package edu.fzu.qujing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.fzu.qujing.bean.Task;
import org.apache.ibatis.annotations.Param;



public interface TaskMapper extends BaseMapper<Task> {
    /**
     * 按照发布人的学号查询任务数量
     *
     * @param studentId 学号
     * @return 任务数量
     */
    public Integer getCountBySenderId(@Param("studentId") String studentId);


    /**
     * 按照接受者的学号查询任务数量
     *
     * @param studentId 学号
     * @return 任务数量
     */
    public Integer getCountByReceiverId(@Param("studentId") String studentId);



    public void updateTask(Task task);
}
