package edu.fzu.qujing.service;

import edu.fzu.qujing.bean.User;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

@Service
public interface UserService {

    /**
     * 保存对密码加密后的user数据
     *
     * @param user 存有user数据
     */
    public Integer saveUser(User user);


    /**
     * 对用户信息进行修改
     *
     * @param user 修改的用户信息
     */
    public void updateUser(User user);

    /**
     * 对用户的接受任务数进行修改
     *
     * @param studentId 学号
     */
    public void updateReceiveTaskNumber(String studentId, int count);

    /**
     * 获得用户已接受的任务数
     *
     * @param studentId 学号
     * @return 用户接受的任务数
     */
    public Integer getUserReceiveTaskNumber(String studentId);


    public User getUserToCheck(User user);
}
