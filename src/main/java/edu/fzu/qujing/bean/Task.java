package edu.fzu.qujing.bean;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author ozg
 * @version 1.0
 */
@Data
@TableName(value = "task")
public class Task {
    //task的ID，自增
    @TableId(type = IdType.AUTO)
    private int id;
    //任务名
    private String name;
    //完成任务可获得积分
    private Integer points;
    //发布时间
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;
    //任务结束时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deadline;
    //是否加急,0:未加急,1:加急
    private Integer expedited;
    //任务状态
    private Integer state;
    //任务详细内容
    private String content;
    //任务类型，1：快递代取，2：文件代送，3：文件代取，4：食堂代买，5：物品代购，6：其他
    private Integer ttid;
    //发布者id
    private String senderid;
    //接收者id
    private String receiverid;
    //数据修改时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //版本号
    @Version
    private Integer version;

}
