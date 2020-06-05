package edu.fzu.qujing.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.fzu.qujing.bean.Task;
import edu.fzu.qujing.bean.User;
import edu.fzu.qujing.component.BloomFilter;
import edu.fzu.qujing.mapper.UserMapper;
import edu.fzu.qujing.service.UserService;
import edu.fzu.qujing.util.BloomFilterUtil;
import edu.fzu.qujing.util.JwtUtil;
import edu.fzu.qujing.util.MailUtil;
import edu.fzu.qujing.util.RedisUtil;
import io.jsonwebtoken.Claims;
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
import java.util.List;

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
                    @CachePut(key = "'getUserByStudentId(' + #user.studentId + ')'",unless = "#result == null"),
                    @CachePut(key = "'getUserByPhone(' + #user.phone + ')'",unless = "#result == null")
            }
    )
    public User save(User user) {
        ByteSource credentialsSalt = ByteSource.Util.bytes(user.getStudentId());
        String pwd = new SimpleHash("MD5",user.getPassword(),
                credentialsSalt,1024).toBase64();
        user.setPassword(pwd);
        user.setState(1);
        userMapper.insert(user);
        BloomFilterUtil.addIfNotExist(BloomFilterUtil.getBloomFilterToUser(),
                user.getStudentId(),"bloomFilterToUser");
        BloomFilterUtil.addIfNotExist(BloomFilterUtil.getBloomFilterToUser()
                ,user.getPhone(),"bloomFilterToUser");
        return user;
    }

    /**
     * 保存用户
     *
     * @param user
     * @return
     */
    @Override
    public void saveUser(User user) {
        String key = "toBeActivated::" + user.getPhone();
        RedisUtil.set(key, user,60 * 10);
        key = "toBeActivated::" + user.getStudentId();
        RedisUtil.set(key, user,60 * 10);
    }

    //change
    /**
     * 对用户的接受任务数进行修改
     *
     * @param studentId 学号
     * @param count
     */
    @Override
    @Caching(
            cacheable = {
                    @Cacheable(key = "'getUserByStudentId(' + #result.studentId + ')'",unless = "#result == null")
            },
            put = {
                    @CachePut(key = "'getUserByStudentId(' + #result.studentId + ')'",unless = "#result == null"),
                    @CachePut(key = "'getUserByPhone(' + #result.phone + ')'",unless = "#result == null")
            }
    )
    public User updateReceiveTaskNumber(String studentId, int count) {
        if(!checkStudentId(studentId)){
            throw new RuntimeException("The server is busy");
        }
        User user = getUserByStudentId(studentId);
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
    @Cacheable(key = "'getUserByStudentId(' + #studentId + ')'",unless = "#result == null")
    @Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
    public User getReceiveTaskNumber(String studentId) {
        if(!checkStudentId(studentId)){
            throw new RuntimeException("The server is busy");
        }
        User user = getUserByStudentId(studentId);
        user.setPassword(null);
        return user;
    }

    @Override
    @Cacheable(key = "#root.methodName + '(' + #root.args + ')'",unless = "#result == null")
    @Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
    public User getUserByStudentId(String studentId) {
        if(!checkStudentId(studentId)){
            throw new RuntimeException("The server is busy");
        }
        User user = new User();
        user.setStudentId(studentId);
        return  userMapper.getUser(user);
    }

    /**
     * 通过手机号查询用于认证的用户信息
     *
     * @return
     */
    @Override
    @Cacheable(key = "#root.methodName + '(' + #root.args + ')'",unless = "#result == null")
    @Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
    public User getUserByPhone(String phone) {
        if(!checkPhone(phone)){
            throw new RuntimeException("The server is busy");
        }
        User user = new User();
        user.setPhone(phone);
        return userMapper.getUser(user);

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
            put = {
                    @CachePut(key = "'getUserByStudentId(' + #result.studentId + ')'",unless = "#result == null"),
                    @CachePut(key = "'getUserByPhone(' + #result.phone + ')'",unless = "#result == null")
            }
    )
    public User updateState(String studentId,Integer state) {
        User userToCheck = getUserByStudentId(studentId);
        userToCheck.setState(state);
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
    @Caching(
            put = {
                    @CachePut(key = "'getUserByStudentId(' + #result.studentId + ')'",unless = "#result == null"),
                    @CachePut(key = "'getUserByPhone(' + #result.phone + ')'",unless = "#result == null")
            }
    )
    public User updatePoints(User user) {
        if(!checkStudentId(user.getStudentId())){
            throw new RuntimeException("The server is busy");
        }
        userMapper.updateById(user);
        return user;
    }



    /**
     * @param studentId
     * @param oldPwd
     * @param newPwd
     * @return
     */
    @Caching(
            put = {
                    @CachePut(cacheNames = "user",key = "'getUserByPhone(' + #result.phone + ')'",unless = "#result == null"),
                    @CachePut(cacheNames = "user",key = "'getUserByStudentId(' + #result.studentId + ')'",unless = "#result == null")
            }
    )
    @Override
    public User updatePassword(String studentId,String oldPwd,String newPwd) {
        if(!checkStudentId(studentId)){
            throw new RuntimeException("The server is busy");
        }

        User user = getUserByStudentId(studentId);
        ByteSource credentialsSalt = ByteSource.Util.bytes(studentId);
        String oldPassword = new SimpleHash("MD5", oldPwd,
                credentialsSalt, 1024).toBase64();

        if(oldPassword.equals(user.getPassword())) {
            String newPassword = new SimpleHash("MD5", newPwd,
                    credentialsSalt, 1024).toBase64();
            if(newPassword.equals(oldPassword)){
                return user;
            }
            user.setPassword(newPassword);
            userMapper.update(user, new QueryWrapper<User>().eq("studentId", studentId));
            return user;
        }

        return null;
    }

    @Override
    @Caching(
        put = {
                @CachePut(cacheNames = "user",key = "'getUserByPhone(' + #result.phone + ')'",unless = "#result == null"),
                @CachePut(cacheNames = "user",key = "'getUserByStudentId(' + #result.studentId + ')'",unless = "#result == null")
        }
    )
    public User updatePassword(String phone, String newPwd) {
        String key = "forgetPwd::flag" + phone;
        if(!checkPhone(phone)||!RedisUtil.hasKey(key)){
            throw new RuntimeException("The server is busy");
        }
        RedisUtil.del(key);

        User user = getUserByPhone(phone);
        ByteSource credentialsSalt = ByteSource.Util.bytes(user.getStudentId());
        String newPassword = new SimpleHash("MD5", newPwd,
                credentialsSalt, 1024).toBase64();
        if(newPassword.equals(user.getPassword())) {
            return user;
        }
        user.setPassword(newPassword);
        userMapper.update(user, new QueryWrapper<User>().eq("studentId", user.getStudentId()));
        return user;
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
    public List<User> listUser() {
        return userMapper.listUser();
    }



    /**
     * 检查手机号是否存在
     *
     * @param phone
     * @return
     */
    @Override
    public boolean checkPhone(String phone) {
        String key = "toBeActivated::" + phone;
        if(RedisUtil.hasKey(key) || BloomFilterUtil.contains(BloomFilterUtil.getBloomFilterToUser(),phone))
        {
            return true;
        }
        return false;
    }

    /**
     * 检查学号是否存在
     *
     * @param studentId
     * @return
     */
    @Override
    public boolean checkStudentId(String studentId) {
        String key = "toBeActivated::" + studentId;
        if(RedisUtil.hasKey(key) || BloomFilterUtil.contains(BloomFilterUtil.getBloomFilterToUser(),studentId))
        {
            return true;
        }
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
    public User getUserInfo(String studentId) {
        if(!checkStudentId(studentId)){
            throw new RuntimeException("The server is busy");
        }
        User user = getUserByStudentId(studentId);
        user.setPassword(null);
        return user;
    }
}
