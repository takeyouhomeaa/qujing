package edu.fzu.qujing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.fzu.qujing.bean.Expenses;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ExpensesMapper extends BaseMapper<Expenses> {
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
     * @param studentId
     * @param page
     * @return 消费记录列表
     */
    IPage<Expenses> listExpense(@Param("userId") String studentId, @Param("page") Page<Expenses> page);
}
