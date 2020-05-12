package edu.fzu.qujing.mapper;

import edu.fzu.qujing.bean.Log;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface LogMapper {
    /**
     * 查询数据条数
     *
     * @return 数据条数
     */
    public Integer getCount();

    /**
     * 添加一条Log
     *
     * @param log log数据
     */
    public void addLog(Log log);


    /**
     * 以分页的方式查询Log
     *
     * @param pos   页数
     * @param pages 数据条数
     * @return Log列表
     */
    public List<Log> listLogByPagination(@Param("pos") Integer pos, @Param("pages") Integer pages);


    /**
     * 更新Log
     *
     * @param log 更新的数据和id
     */
    public void updateLog(Log log);

}
