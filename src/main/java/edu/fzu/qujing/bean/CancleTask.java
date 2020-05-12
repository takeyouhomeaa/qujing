package edu.fzu.qujing.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;


public class CancleTask {

    private int id;
    private String content;

    private Task task;
    //0->未被处理、1->已被处理
    private int flat;

    private CancleType type;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public int getFlat() {
        return flat;
    }

    public void setFlat(int flat) {
        this.flat = flat;
    }

    public CancleType getType() {
        return type;
    }

    public void setType(CancleType type) {
        this.type = type;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
