package edu.fzu.qujing.service;

import edu.fzu.qujing.bean.Expenses;
import edu.fzu.qujing.bean.Recharge;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface SettleService {


    void updatePoints(String studentId,Integer amount);

    /**
     * 增加用户积分
     *
     * @param studentId 学号
     * @param amount    金额
     */
    void increasePoints(String studentId, Integer amount);

    /**
     * 减少用户积分
     *
     * @param studentId 学号
     * @param amount    金额
     */
    void decreasePoints(String studentId, Integer amount);


    /**
     * 添加充值记录
     *
     * @param recharge 充值数据
     */
    Recharge addRechargeRecord(Recharge recharge);


    /**
     * 添加积分消费记录
     *
     * @param expenses 消费数据
     */
    Expenses addExpensesRecord(Expenses expenses);


    /**
     * 以分页的方式查询消费记录
     *
     * @param studentId
     * @param pos      数据行数
     * @return 消费记录列表
     */
    List<Expenses> listExpensesRecord(String studentId, Integer pos);


    /**
     * @param studentId
     * @param pos      数据行数
     * @return 充值记录列表
     */
    List<Recharge> listRecharheRecord(String studentId, Integer pos);
}
