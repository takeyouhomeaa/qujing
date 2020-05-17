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
                    @CachePut(key = "#user.studentId + '_check'"),
                    @CachePut(key = "#user.email + '_check'")}
    )
    public Integer save(User user) {
        ByteSource credentialsSalt = ByteSource.Util.bytes(user.getStudentId());
        String pwd = new SimpleHash("MD5",user.getPassword(),
                credentialsSalt,1024).toBase64();
        user.setPassword(pwd);
        userMapper.insert(user);
        return user.getId();
    }


    /**
     * 对用户的接受任务数进行修改
     *
     * @param studentId 学号
     * @param count
     */
    @Override
    @Caching(
            cacheable = {@Cacheable(key = "#studentId + '_ReceiveTaskNumbe'")},
            put = {@CachePut(key = "#studentId + '_ReceiveTaskNumbe'")}
    )
    public Integer updateReceiveTaskNumber(String studentId, int count) {
        User user = userMapper.getNumberOfTasksAccepted(studentId);
        user.setReceiveTaskNumber(user.getReceiveTaskNumber() + count);
        userMapper.updateById(user);
        return user.getReceiveTaskNumber();
    }

    /**
     * 获得用户已接受的任务数
     *
     * @param studentId 学号
     * @return 用户接受的任务数
     */
    @Override
    @Cacheable(key = "#studentId + '_ReceiveTaskNumbe'")
    @Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
    public Integer getReceiveTaskNumber(String studentId) {
        User user = userMapper.getNumberOfTasksAccepted(studentId);
        return user.getReceiveTaskNumber();
    }

    @Override
    @Cacheable(key = "#studentId + '_check'")
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
    @Cacheable(key = "#email + '_check'")
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
            cacheable = {@Cacheable(key = "#studentId + '_check'")},
            put = {@CachePut(key = "#studentId + '_check'")}
    )
    public User updateState(String studentId,Integer state) {
        User user = new User();
        user.setStudentId(studentId);
        User userToCheck = userMapper.getUserToCheck(user);
        userMapper.updateById(userToCheck);
       return userToCheck;
    }
}
