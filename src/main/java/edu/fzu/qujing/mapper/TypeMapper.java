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
    List<Type> listTaskType();

    /**
     * 获取任务状态列表
     * @return
     */
    List<Type> listTaskState();

    /**
     * 通过类型id获取任务状态
     * @param id
     * @return
     */
    Type getTakeState(Integer id);

    /**
     * 通过类型id获取任务状态
     *
     * @param id
     * @return
     */
    Type getTaskType(Integer id);


    /**
     * 获取消费记录列表
     *
     * @return
     */
    List<Type> listExpensesType();


    /**
     * 通过类型id获取消费记录类型
     *
     * @param id
     * @return
     */
    Type getExpensesType(Integer id);
}
