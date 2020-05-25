package edu.fzu.qujing.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@ApiModel(description = "延迟任务")
public class DelayTask implements Delayed {

    @ApiModelProperty("任务编号")
    private Integer id;

    @ApiModelProperty("学号")
    private String studentId;

    @ApiModelProperty("延迟时间")
    private long excuteTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public DelayTask(Integer id) {
        this.id = id;
    }

    public DelayTask() {
    }

    public long getExcuteTime() {
        return excuteTime;
    }

    public void setExcuteTime(long excuteTime) {
        this.excuteTime = excuteTime;
    }

    public DelayTask(Integer id, String studentId ,long excuteTime) {
        this.id = id;
        this.studentId = studentId;
        this.excuteTime = excuteTime + System.currentTimeMillis();
    }

    @Override
    public long getDelay(TimeUnit timeUnit) {
        return timeUnit.convert(this.excuteTime - System.currentTimeMillis(), TimeUnit.NANOSECONDS);

    }

    @Override
    public int compareTo(Delayed delayed) {
        return (int) (this.getDelay(TimeUnit.NANOSECONDS) - delayed.getDelay(TimeUnit.NANOSECONDS));
    }
}
