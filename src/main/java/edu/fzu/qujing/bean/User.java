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
@ApiModel(description = "User实体")
public class User implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    //用户登录邮箱
    @ApiModelProperty("用户邮箱")
    private String email;
    //用户名
    @ApiModelProperty("用户昵称邮箱")
    private String username;
    //学号
    @ApiModelProperty("用户学号")
    private String studentId;
    //密码
    @ApiModelProperty("用户密码")
    private String password;
    //用户所拥有的积分
    private Integer points;
    //用户账户状态，0:未激活（注销状态），1：激活，2： 冻结
    private Integer state;
    //账户的详细信息
    private String content;
    //用户账户冻结状态的结束时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    //用户已接收任务数
    private Integer receiveTaskNumber;
    //用户恶意接收的任务数量
    private Integer maliciousAcceptanceNumber;
    //用户被成功举报的数量
    private Integer reportedNumber;
    //注册时间
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;
    //数据上次更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateDate;
    //版本号
    @Version
    private Integer version;


    //接收的任务
    private List<Task> acceptTask;
    //发布的任务
    private List<Task> publishTask;
}
