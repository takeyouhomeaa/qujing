package edu.fzu.qujing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.fzu.qujing.bean.User;

import java.util.List;


public interface UserMapper extends BaseMapper<User> {
    /**
     * 查找一个用户身份认证的User
     * @param user email和IDNumber
     * @return User
     */
    User getUser(User user);

    /**
     * 获取用户列表
     *
     * @return
     */
    List<User> listUser();



    /**
     * 更改用户信息
     *
     * @param user
     */
    void updateUser(User user);




    /**
     * 获得手机号
     * @param phone
     * @return
     */
    User getPhone(String phone);


    /**
     * 获取学号
     * @param studentId
     * @return
     */
    User getStudentId(String studentId);





}
