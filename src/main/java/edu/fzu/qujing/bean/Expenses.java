package edu.fzu.qujing.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

/**
 * @version 1.1
 * 1.1：
 * 把邮箱关联用户改为用户ID
 */
public class Expenses {
    //消费记录的ID，自增
    public Integer id;
    //消费金额
    public Integer amount;
    //消费时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date time;
    //消费者Id
    public String userId;
    //消费类型，1：发布任务，2：提现，3：惩罚
    public Type type;
    //消费类型列表
    public List<Type> typeList;

    //无参构造器
    public Expenses() {
    }

    //包含无裂变字段的构造器
    public Expenses(Integer id, Integer amount, Date time, String userId, Type type) {
        this.id = id;
        this.amount = amount;
        this.time = time;
        this.userId = userId;
        this.type = type;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Type> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<Type> typeList) {
        this.typeList = typeList;
    }

    @Override
    public String toString() {
        return "Expenses{" +
                "id=" + id +
                ", amount=" + amount +
                ", time=" + time +
                ", userId=" + userId +
                ", type=" + type +
                ", typeList=" + typeList +
                '}';
    }
}
