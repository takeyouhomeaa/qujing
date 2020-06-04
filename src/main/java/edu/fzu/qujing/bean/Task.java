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
 * @version 1.0
 */
@Data
@ApiModel(description = "任务")
@TableName(value = "task")
public class Task implements Serializable {

    @ApiModelProperty("ID")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("任务名")
    private String name;

    @ApiModelProperty("完成任务可获得积分")
    private Integer points;

    @ApiModelProperty("发布时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;

    @ApiModelProperty("任务结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deadline;

    @ApiModelProperty("是否加急,0:未加急,1:加急")
    private Integer expedited;

    @ApiModelProperty("任务状态")
    private Integer state;

    @ApiModelProperty("任务详细内容")
    private String content;

    @ApiModelProperty("任务类型，1：快递代取 ，2：文件代送，3：食堂代买，4：物品代购，5：其他")
    private Integer ttid;

    @ApiModelProperty("发布者id")
    private String senderid;

    @ApiModelProperty("接收者id")
    private String receiverid;

    @ApiModelProperty("数据修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty("版本号")
    @Version
    private Integer version;

}
