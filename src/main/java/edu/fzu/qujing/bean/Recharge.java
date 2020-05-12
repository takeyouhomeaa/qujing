package edu.fzu.qujing.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @version 1.1
 * 把邮箱关联用户改为用户ID
 */
public class Recharge {
    //消费记录的ID，自增
    public Integer id;
    //消费金额
    public Integer amount;
    //消费时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date time;
    //消费者id
    public String userId;
    //

    //空的构造方法
    public Recharge() {
    }

    //全字段的构造方法


    public Recharge(Integer id, Integer amount, Date time, String userId) {
        this.id = id;
        this.amount = amount;
        this.time = time;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Recharge{" +
                "id=" + id +
                ", amount=" + amount +
                ", time=" + time +
                ", userId=" + userId +
                '}';
    }
}