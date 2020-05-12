package edu.fzu.qujing.mapper;

import edu.fzu.qujing.bean.Type;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version 1.0
 */

public interface TypeMapper {

    /**
     * 查找全部的任务类型
     *
     * @return 查找到的TaskType列表
     */
    public List<Type> listTaskType();

    /**
     * 查找全部的二级任务类型
     *
     * @param taskTypeId TaskType列表
     * @return 查找到的NextTaskType列表
     */
    public List<Type> listNextTaskType(@Param("taskTypeId") Integer taskTypeId);
}
