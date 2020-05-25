package edu.fzu.qujing.bean;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author
 */
@Data
@TableName(value = "feedback")
@ApiModel(description = "反馈信息")
public class FeedBack implements Serializable {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键")
    private int id;

    @ApiModelProperty("反馈内容")
    private String content;

    @ApiModelProperty("任务ID")
    private Task task;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date time;

    @ApiModelProperty("反馈类型")
    private Integer type;

    @ApiModelProperty("0->未被处理、1->已被处理")
    private Integer flat;

    @Version
    @ApiModelProperty("版本号")
    private Integer version;

    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
