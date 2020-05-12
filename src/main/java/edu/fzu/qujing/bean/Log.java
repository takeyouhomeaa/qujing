package edu.fzu.qujing.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.Map;

public class Log {
    //日志主键
    private Integer id;
    //日志类型
    private String type;
    //日志标题
    private String title;
    //请求地址
    private String remoteAddr;
    //URI
    private String requestUri;
    //请求方式
    private String method;
    //提交参数
    private String params;
    //异常
    private String exception;
    //开始时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date operateDate;
    //结束时间
    private String timeout;
    //用户ID
    private String userId;

    public Log() {
    }

    public Integer getLogId() {
        return id;
    }

    public void setLogId(Integer logId) {
        this.id = logId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public Date getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(Date operateDate) {
        this.operateDate = operateDate;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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
            params.append(param.getKey().equals("password") ? "" : paramValue.substring(0, 50));
        }
        this.params = params.toString();
    }

    @Override
    public String toString() {
        return "Log{" +
                "logId='" + id + '\'' +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", remoteAddr='" + remoteAddr + '\'' +
                ", requestUri='" + requestUri + '\'' +
                ", method='" + method + '\'' +
                ", params='" + params + '\'' +
                ", exception='" + exception + '\'' +
                ", operateDate=" + operateDate +
                ", timeout='" + timeout + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }


}
