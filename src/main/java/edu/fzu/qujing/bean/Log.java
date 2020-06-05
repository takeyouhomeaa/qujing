package edu.fzu.qujing.bean;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Data
@TableName(value = "log")
@ApiModel(description = "日志")
public class Log implements Serializable {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("日志类型")
    private String type;

    @ApiModelProperty("日志标题")
    private String title;

    @ApiModelProperty("请求地址")
    private String remoteAddr;

    @ApiModelProperty("请求URI")
    private String requestUri;

    @ApiModelProperty("请求方式")
    private String method;

    @ApiModelProperty("提交参数")
    private String params;

    @ApiModelProperty("异常")
    private String exception;

    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date operateDate;

    @ApiModelProperty("结束时间")
    private String timeout;

    @ApiModelProperty("用户ID")
    private String userId;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("更新时间")
    private Date updateTime;

    @Version
    @ApiModelProperty("版本")
    private Integer version;


    /**
     * 设置请求参数
     *
     * @param paramMap 请求参数列表
     */
    public void setMapToParams(Map<String, String[]> paramMap) {
        if (paramMap == null) {
            return;
        }
        StringBuilder params = new StringBuilder();
        for (Map.Entry<String, String[]> param : ((Map<String, String[]>) paramMap).entrySet()) {
            params.append(("".equals(params.toString()) ? "" : "&") + param.getKey() + "=");
            String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
            params.append("password".equals(param.getKey()) ? "" : paramValue);
        }
        this.params = params.toString();
    }



}
