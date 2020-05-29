package edu.fzu.qujing.service;

import edu.fzu.qujing.bean.Message;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MessageService {

    /**
     * 保存数据
     *
     * @param message
     * @return
     */
    Message save(Message message);

    /**
     * 查询接收的消息列表
     *
     * @param studentId
     * @param pos
     * @return
     */
    List<Message> listMessageByReceiveId(String studentId,Integer pos);


    /**
     * 查询发布的消息列表
     *
     * @param studentId
     * @param pos
     * @return
     */
    List<Message> listMessageByPublisherId(String studentId,Integer pos);

}
