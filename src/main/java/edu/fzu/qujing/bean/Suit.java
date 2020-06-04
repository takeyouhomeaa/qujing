package edu.fzu.qujing.bean;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "suit")
@ApiModel(description = "任务举报")
public class Suit {

    @ApiModelProperty("ID")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("举报内容")
    private String content;

    @TableField("taskId")
    @ApiModelProperty("任务id")
    private Integer task;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date time;

    @ApiModelProperty("举报类型")
    private Integer type;

    @ApiModelProperty("已读标志,0:未读，1:已读")
    private Integer flat;

    @ApiModelProperty("数据修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty("版本号")
    @Version
    private Integer version;

}
