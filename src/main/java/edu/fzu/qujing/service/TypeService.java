package edu.fzu.qujing.service;

import edu.fzu.qujing.bean.Type;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TypeService {

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
     * 通过类型id获取消费记录类型
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



    /**
     * 获得欺诈反馈类型列表
     *
     * @return
     */
    List<Type> listFeedBackTpe();


    /**
     * 获得举报类型列表
     *
     * @return
     */
    List<Type> listSuitType();


    /**
     * 通过类型id获取欺诈反馈类型
     *
     * @param id
     * @return
     */
    Type getFeedBackTpe(Integer id);

    /**
     * 通过类型id获得举报类型
     *
     * @return
     */
    Type getSuitType(Integer id);


}
