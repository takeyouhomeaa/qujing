package edu.fzu.qujing.mapper;

import edu.fzu.qujing.bean.FeedBack;


/**
 * @version 1.0
 */

public interface FeedBackMapper {
    /**
     * 查询数量
     *
     * @return 条数
     */
    public Integer getCount();

    /**
     * 添加一条feedback数据
     *
     * @param feedBack FeedBack数据
     */
    public void addFeedBack(FeedBack feedBack);

}
