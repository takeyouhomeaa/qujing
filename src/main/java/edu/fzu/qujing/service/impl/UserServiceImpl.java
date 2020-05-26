package edu.fzu.qujing.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.fzu.qujing.bean.User;
import edu.fzu.qujing.mapper.UserMapper;
import edu.fzu.qujing.service.UserService;
import edu.fzu.qujing.util.MailUtil;
import edu.fzu.qujing.util.RedisUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.naming.AuthenticationException;

@Service
@CacheConfig(cacheNames = "user")
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
    @Caching(
            put = {
                    @CachePut(key = "'getUserToCheckByStudentId(' + #user.studentId + ')'"),
                    @CachePut(key = "'getUserToCheckByEmail(' + #user.email + ')'")}
    )
    public User save(User user) {
        ByteSource credentialsSalt = ByteSource.Util.bytes(user.getStudentId());
        String pwd = new SimpleHash("MD5",user.getPassword(),
                credentialsSalt,1024).toBase64();
        user.setPassword(pwd);
        userMapper.insert(user);
        return user;
    }

    /**
     * 保存用户
     *
     * @param user
     * @return
     */
    @Override
    public Integer saveUser(User user) {
        return save(user).getId();
    }

    /**
     * 对用户的接受任务数进行修改
     *
     * @param studentId 学号
     * @param count
     */
    @Override
    @Caching(
            cacheable = {@Cacheable(key = "'getNumberOfTasksAccepted' + '(' + #studentId + ')'")},
            put = {@CachePut(key = "'getNumberOfTasksAccepted' + '(' + #studentId + ')'")}
    )
    public User updateReceiveTaskNumber(String studentId, int count) {
        User user = userMapper.getNumberOfTasksAccepted(studentId);
        user.setReceiveTaskNumber(user.getReceiveTaskNumber() + count);
        userMapper.updateById(user);
        return user;
    }

    /**
     * 获得用户已接受的任务数
     *
     * @param studentId 学号
     * @return 用户接受的任务数
     */
    @Override
    @Cacheable(key = "#root.methodName + '(' + #root.args + ')'")
    @Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
    public User getReceiveTaskNumber(String studentId) {
        User user = userMapper.getNumberOfTasksAccepted(studentId);
        return user;
    }

    @Override
    @Cacheable(key = "#root.methodName + '(' + #root.args + ')'")
    @Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
    public User getUserToCheckByStudentId(String studentId) {
        User user = new User();
        user.setStudentId(studentId);
        return  userMapper.getUserToCheck(user);
    }

    /**
     * 通过邮箱查询用于认证的用户信息
     *
     * @param email
     * @return
     */
    @Override
    @Cacheable(key = "#root.methodName + '(' + #root.args + ')'")
    @Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
    public User getUserToCheckByEmail(String email) {
        User user = new User();
        user.setEmail(email);
        return userMapper.getUserToCheck(user);

    }


    /**
     * 修改用户账户状态
     *
     * @param studentId
     * @param state
     * @return
     */
    @Override
    @Caching(
            cacheable = {
                    @Cacheable(key = "'getUserToCheckByStudentId(' + #studentId + ')'")
            },
            put = {
                    @CachePut(key = "'getUserToCheckByStudentId(' + #studentId + ')'")
            }
    )
    public User updateState(String studentId,Integer state) {
        User userToCheck = getUserToCheckByStudentId(studentId);
        userMapper.updateById(userToCheck);
       return userToCheck;
    }

    /**
     * 修改用户的积分
     *
     * @param user
     * @return
     */
    @Override
    @CachePut(key = "'getUserPoints(' + #user.studentId +')'")
    public User updatePoints(User user) {
        userMapper.updateById(user);
        return user;
    }

    /**
     * 获取用户所有的积分
     *
     * @param studentId
     * @return
     */
    @Cacheable(key = "#root.methodName + '(' + #root.args + ')'")
    @Override
    public User getUserPoints(String studentId) {
        return userMapper.getUserPoints(studentId);
    }
}
