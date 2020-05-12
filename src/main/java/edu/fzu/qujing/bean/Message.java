package edu.fzu.qujing.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author ozg
 * 1.1：
 * 把邮箱关联用户改为用户ID
 * @version 1.1
 */
@Data
public class Message {
    //Message的ID，自增
    private int id;
    //消息的详细内容
    private String content;
    //是否已读,0:未读,1:已读
    private int messageState;
    //消息类型，1：系统通知，2：用户通知
    private MessageType messageType;
    //接收消息的用户邮箱
    private String receiveId;
    //发送消息的用户邮箱(系统消息为 0)
    private String publisherId;
    //时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date time;

}
