package edu.fzu.qujing.bean;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "cancletask")
@ApiModel(description = "取消任务的理由信息")
public class CancelTask implements Serializable {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键")
    private int id;

    @ApiModelProperty("取消任务理由")
    private String content;

    @ApiModelProperty("任务ID")
    private Integer task;

    @ApiModelProperty("是否已读标志, 0:未读，1：已读")
    private int flat;

    @ApiModelProperty("理由的类型")
    private Integer type;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;

    @Version
    @ApiModelProperty("版本号")
    private Integer version;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("修改时间")
    private Date updateTime;


}
