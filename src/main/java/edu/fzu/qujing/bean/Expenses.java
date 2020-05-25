package edu.fzu.qujing.bean;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * @author
 */
@Data
@TableName(value = "expensesrecord")
@ApiModel(description = "消费记录")
public class Expenses implements Serializable {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("消费记录的ID，自增")
    private Integer id;

    @ApiModelProperty("消费金额")
    private Integer amount;

    @ApiModelProperty("消费者Id")
    private String userId;

    @ApiModelProperty("消费类型，1：发布任务，2：提现，3：惩罚")
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
