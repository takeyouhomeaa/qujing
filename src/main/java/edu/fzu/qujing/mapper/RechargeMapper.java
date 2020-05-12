package edu.fzu.qujing.mapper;

import edu.fzu.qujing.bean.Recharge;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface RechargeMapper {
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
     * @param recharge 储存有查询条件
     * @return 充值记录的数据列表
     */
    public List<Recharge> listRechargeRecord(@Param("recharge") Recharge recharge,
                                             @Param("pos") Integer pos,
                                             @Param("time") String time,
                                             @Param("pages") Integer pages);
}
