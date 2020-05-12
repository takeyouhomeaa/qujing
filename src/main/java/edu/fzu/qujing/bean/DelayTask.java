package edu.fzu.qujing.bean;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayTask implements Delayed {

    //任务编号
    private Integer id;
    //延迟时间
    private long excuteTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public DelayTask(Integer id, long excuteTime) {
        this.id = id;
        this.excuteTime = excuteTime + System.currentTimeMillis();
    }

    @Override
    public long getDelay(TimeUnit timeUnit) {
        return timeUnit.convert(this.excuteTime - System.currentTimeMillis(), TimeUnit.NANOSECONDS);

    }

    @Override
    public int compareTo(Delayed delayed) {
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) - delayed.getDelay(TimeUnit.MILLISECONDS));
    }
}
