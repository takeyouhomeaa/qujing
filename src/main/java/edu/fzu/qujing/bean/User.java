package edu.fzu.qujing.bean;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName(value = "user")
@ApiModel(description = "User")
public class User implements Serializable {

    @ApiModelProperty("用户ID")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户邮箱")
    private String email;

    @ApiModelProperty("用户昵称邮箱")
    private String username;

    @ApiModelProperty("用户学号")
    private String studentId;

    @ApiModelProperty("用户密码")
    private String password;

    @ApiModelProperty("用户所拥有的积分")
    private Integer points;

    @ApiModelProperty("用户账户状态，0:未激活（注销状态），1：激活，2： 冻结")
    private Integer state;

    @ApiModelProperty("账户的详细信息")
    private String content;

    @ApiModelProperty("用户账户冻结状态的结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty("用户已接收任务数")
    private Integer receiveTaskNumber;

    @ApiModelProperty("用户恶意接收的任务数量")
    private Integer maliciousAcceptanceNumber;

    @ApiModelProperty("用户被成功举报的数量")
    private Integer reportedNumber;

    @ApiModelProperty("注册时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;

    @ApiModelProperty("数据上次更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty("版本号")
    @Version
    private Integer version;
}
