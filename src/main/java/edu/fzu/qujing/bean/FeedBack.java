package edu.fzu.qujing.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

/**
 * @version 1.0
 */
@Data
public class FeedBack {
    private int id;
    private String content;
    private Task task;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date time;

    private FeedBackType type;
    //0->未被处理、1->已被处理
    private int flat;

    public FeedBack() {
    }

}
