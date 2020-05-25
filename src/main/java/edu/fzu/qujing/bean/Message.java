package edu.fzu.qujing.bean;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ozg
 *
 */
@Data
@TableName(value = "message")
@ApiModel(description = "消息")
public class Message implements Serializable {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键")
    private int id;

    @ApiModelProperty("消息的详细内容")
    private String content;

    @ApiModelProperty("是否已读,0:未读,1:已读")
    private Integer messageState;

    @ApiModelProperty("消息类型，1：系统通知，2：用户通知")
    private Integer messageType;

    @ApiModelProperty("接收消息的用户邮箱")
    private String receiveId;

    @ApiModelProperty("发送消息的用户邮箱(系统消息为 0)")
    private String publisherId;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;

    @Version
    @ApiModelProperty("版本号")
    private Integer version;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
