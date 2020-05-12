package edu.fzu.qujing.mapper;

import edu.fzu.qujing.bean.Message;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageMapper {
    /**
     * 查询接收者数据条数
     *
     * @param receiveId 学号
     * @return 数据条数
     */
    public Integer getCountToReceiveId(@Param("receiveId") String receiveId);

    /**
     * 查询发布者数据条数
     *
     * @param publisherId 学号
     * @return 数据条数
     */
    public Integer getCountToPublisherId(@Param("publisherId") String publisherId);

    /**
     * 添加Message数据
     *
     * @param message Message数据
     */
    public void addMessage(Message message);

    /**
     * 按条件查询分页Message
     *
     * @param message 查询条件
     * @return Message列表
     */
    public List<Message> listMessage(@Param("message") Message message,
                                     @Param("pos") Integer pos,
                                     @Param("pages") Integer pages);
}
