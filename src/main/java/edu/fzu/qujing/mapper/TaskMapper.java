package edu.fzu.qujing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.fzu.qujing.bean.Task;
import org.apache.ibatis.annotations.Param;



public interface TaskMapper extends BaseMapper<Task> {
    /**
     * 按照发布人的学号查询任务数量
     *
     * @param studentId 学号
     * @return 任务数量
     */
    Integer getCountBySenderId(@Param("studentId") String studentId);


    /**
     * 按照接受者的学号查询任务数量
     *
     * @param studentId 学号
     * @return 任务数量
     */
    Integer getCountByReceiverId(@Param("studentId") String studentId);


    /**
     * 查询的任务大概信息
     * @return
     */
    IPage<Task> listSimpleTask(Page<Task> page,@Param("state") Integer state);

    /**
     * 通过任务ID 查找对应任务的详细信息
     * @param task
     * @return
     */
    Task getTaskById(Task task);



}
