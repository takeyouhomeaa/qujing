package edu.fzu.qujing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.fzu.qujing.bean.Expenses;
import edu.fzu.qujing.bean.Recharge;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface RechargeMapper extends BaseMapper<Recharge> {
    /**
     * 查询数据条数
     *
     * @return 数据条数
     */
    public Integer getCount(@Param("userId") String userId);

    /**
     * 添加一条充值记录
     *
     * @param recharge 充值记录的数据
     */
    public void addRecharge(Recharge recharge);

    /**
     * 按条件查找充值记录
     *
     * @param studentId
     * @param page
     * @return 充值记录的数据列表
     */
    IPage<Recharge> listRechargeRecord(@Param("userId") String studentId, @Param("page") Page<Recharge> page);
}
