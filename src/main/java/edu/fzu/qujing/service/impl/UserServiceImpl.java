package edu.fzu.qujing.service.impl;

import edu.fzu.qujing.bean.User;
import edu.fzu.qujing.mapper.UserMapper;
import edu.fzu.qujing.service.UserService;
import edu.fzu.qujing.util.MailUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.naming.AuthenticationException;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;
    /**
     * 保存对密码加密后的user数据
     *
     * @param user 存有user数据
     */
    @Override
    public Integer saveUser(User user) {
        ByteSource credentialsSalt = ByteSource.Util.bytes(user.getStudentId());
        String pwd = new SimpleHash("MD5",user.getPassword(),credentialsSalt,1024).toBase64();
        userMapper.insert(user);
        return user.getId();
    }

    /**
     * 对用户信息进行修改
     *
     * @param user 修改的用户信息
     */
    @Override
    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

    /**
     * 对用户的接受任务数进行修改
     *
     * @param studentId 学号
     * @param count
     */
    @Override
    public void updateReceiveTaskNumber(String studentId, int count) {

    }

    /**
     * 获得用户已接受的任务数
     *
     * @param studentId 学号
     * @return 用户接受的任务数
     */
    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
    public Integer getUserReceiveTaskNumber(String studentId) {
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
    public User getUserToCheck(User user) {
        User userToCheck = userMapper.getUserToCheck(user);
        return userToCheck;
    }
}
