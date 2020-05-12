package edu.fzu.qujing.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;


public class Suit {

    private int id;
    private String content;

    private Task task;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date time;

    private SuitType type;
    private int flat;

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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public SuitType getType() {
        return type;
    }

    public void setType(SuitType type) {
        this.type = type;
    }

    public int getFlat() {
        return flat;
    }

    public void setFlat(int flat) {
        this.flat = flat;
    }
}
