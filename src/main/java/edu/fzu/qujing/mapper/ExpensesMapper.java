package edu.fzu.qujing.mapper;

import edu.fzu.qujing.bean.Expenses;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ExpensesMapper {
    /**
     * 查询数据条数
     *
     * @param userId 学号
     * @return 数据条数
     */
    public Integer getCount(@Param("userId") String userId);

    /**
     * 添加一条expense
     *
     * @param expenses 消费记录数据
     */
    public void addExpensesRecord(Expenses expenses);


    /**
     * 按条件查询消费记录
     *
     * @param expenses 存储查询条件
     * @return 消费记录列表
     */
    public List<Expenses> listExpense(@Param("expenses") Expenses expenses,
                                      @Param("pos") Integer pos,
                                      @Param("time") String time,
                                      @Param("pages") Integer pages);
}
