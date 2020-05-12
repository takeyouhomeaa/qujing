package edu.fzu.qujing.bean;


import lombok.Data;

@Data
public class FeedBackType {

    private int id;
    private String type;

    public FeedBackType() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
