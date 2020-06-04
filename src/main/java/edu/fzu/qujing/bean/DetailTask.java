package edu.fzu.qujing.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "详细的任务类型")
@Data
public class DetailTask {
    @ApiModelProperty("任务信息")
    Task task;
    @ApiModelProperty("发布者信息")
    User sender;
    @ApiModelProperty("接收者信息")
    User receiver;

}
