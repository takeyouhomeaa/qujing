package edu.fzu.qujing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.fzu.qujing.bean.User;



public interface UserMapper extends BaseMapper<User> {
    /**
     * 查找一个用户身份认证的User
     * @param user email和IDNumber
     * @return User
     */
    User getUserToCheck(User user);

    /**
     * @param user
     */
    void updateUser(User user);


    /**
     * 查询已接受的任务数
     * @param studentId 学号
     * @return
     */
    User getNumberOfTasksAccepted(String studentId);



}