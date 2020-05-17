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
    public Integer save(User user);


    /**
     * 对用户的接受任务数进行修改
     *
     * @param studentId 学号
     * @param count   任务数
     * @return
     */
    Integer updateReceiveTaskNumber(String studentId, int count);

    /**
     * 获得用户已接受的任务数
     *
     * @param studentId 学号
     * @return 用户接受的任务数
     */
    Integer getReceiveTaskNumber(String studentId);


    /**
     * 通过学号查询用于认证的用户信息
     *
     * @param studentId
     * @return
     */
    User getUserToCheckByStudentId(String studentId);

    /**
     * 通过邮箱查询用于认证的用户信息
     *
     * @param email
     * @return
     */
    User getUserToCheckByEmail(String email);


    /**
     * 修改用户账户状态
     *
     * @param studentId
     * @param state
     * @return
     */
    User updateState(String studentId,Integer state);
}
