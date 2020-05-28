package edu.fzu.qujing.service;

import com.baomidou.mybatisplus.extension.service.IService;
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
    public User save(User user);

    /**
     * 保存用户
     *
     * @param user
     * @return
     */
    void saveUser(User user);


    /**
     * 对用户的接受任务数进行修改
     *
     * @param studentId 学号
     * @param count     任务数
     * @return
     */
    User updateReceiveTaskNumber(String studentId, int count);

    /**
     * 获得用户已接受的任务数
     *
     * @param studentId 学号
     * @return 用户接受的任务数
     */
    User getReceiveTaskNumber(String studentId);


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
    User getUserToCheckByPhone(String email);


    /**
     * 修改用户账户状态
     *
     * @param studentId
     * @param state
     * @return
     */
    User updateState(String studentId, Integer state);


    /**
     * 修改用户的积分
     *
     * @param user
     * @return
     */
    User updatePoints(User user);


    /**
     * 获取用户所有的积分
     *
     * @param studentId
     * @return
     */
    User getUserPoints(String studentId);


    /**
     * 修改密码
     *
     * @param studentId
     * @param oldPwd
     * @param newPwd
     * @return
     */
    User updatePassword(String studentId, String oldPwd, String newPwd);


    /**
     * 修改密码
     *
     * @param phone
     * @param newPwd
     * @return
     */
    User updatePassword(String phone, String newPwd);

    /**
     * 检查学号是否存在
     *
     * @param studentId
     * @return
     */
    boolean checkStudentId(String studentId);

    /**
     * 检查手机号是否存在
     *
     * @param phone
     * @return
     */
    boolean checkPhone(String phone);
}
