package edu.fzu.qujing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.fzu.qujing.bean.Type;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version 1.0
 */

public interface TypeMapper extends BaseMapper<Type> {
    /**
     * 获取任务类型列表
     *
     * @return
     */
    public List<Type> listTaskType();

    /**
     * 获取任务状态列表
     * @return
     */
    public List<Type> listTaskState();

    /**
     * 通过类型id获取任务状态
     * @param id
     * @return
     */
    public Type getTakeState(Integer id);

    /**
     * 通过类型id获取任务状态
     *
     * @param id
     * @return
     */
    public Type getTaskType(Integer id);
}
