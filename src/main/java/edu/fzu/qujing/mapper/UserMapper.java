package edu.fzu.qujing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.fzu.qujing.bean.User;



public interface UserMapper extends BaseMapper<User> {
    /**
     * 查找一个用户身份认证的User
     * @param user email和IDNumber
     * @return User
     */
    User getUserToCheck(User user);

    void updateUser(User user);
}
