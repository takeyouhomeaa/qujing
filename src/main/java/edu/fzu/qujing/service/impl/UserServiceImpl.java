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
                    @CachePut(key = "'getUserToCheckByStudentId(' + #user.studentId + ')'",unless = "#result == null"),
                    @CachePut(key = "'getUserToCheckByPhone(' + #user.phone + ')'",unless = "#result == null")
            }
    )
    public User save(User user) {
        ByteSource credentialsSalt = ByteSource.Util.bytes(user.getStudentId());
        String pwd = new SimpleHash("MD5",user.getPassword(),
                credentialsSalt,1024).toBase64();
        user.setPassword(pwd);
        user.setState(1);
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
    public void saveUser(User user) {
        String key = "toBeActivated::" + user.getPhone();
        RedisUtil.set(key, user,60 * 10);
    }

    /**
     * 对用户的接受任务数进行修改
     *
     * @param studentId 学号
     * @param count
     */
    @Override
    @Caching(
            cacheable = {
                    @Cacheable(key = "'getNumberOfTasksAccepted' + '(' + #studentId + ')'",unless = "#result == null")
            },
            put = {
                    @CachePut(key = "'getNumberOfTasksAccepted' + '(' + #studentId + ')'",unless = "#result == null")
            }
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
    @Cacheable(key = "#root.methodName + '(' + #root.args + ')'",unless = "#result == null")
    @Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
    public User getReceiveTaskNumber(String studentId) {
        User user = userMapper.getNumberOfTasksAccepted(studentId);
        return user;
    }

    @Override
    @Cacheable(key = "#root.methodName + '(' + #root.args + ')'",unless = "#result == null")
    @Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
    public User getUserToCheckByStudentId(String studentId) {
        User user = new User();
        user.setStudentId(studentId);
        return  userMapper.getUserToCheck(user);
    }

    /**
     * 通过手机号查询用于认证的用户信息
     *
     * @return
     */
    @Override
    @Cacheable(key = "#root.methodName + '(' + #root.args + ')'",unless = "#result == null")
    @Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
    public User getUserToCheckByPhone(String phone) {
        User user = new User();
        user.setPhone(phone);
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
                    @Cacheable(key = "'getUserToCheckByStudentId(' + #studentId + ')'",unless = "#result == null")
            },
            put = {
                    @CachePut(key = "'getUserToCheckByStudentId(' + #studentId + ')'",unless = "#result == null")
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
    @CachePut(key = "'getUserPoints(' + #user.studentId +')'",unless = "#result == null")
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
    @Cacheable(key = "#root.methodName + '(' + #root.args + ')'",unless = "#result == null")
    @Override
    public User getUserPoints(String studentId) {
        return userMapper.getUserPoints(studentId);
    }


    /**
     * @param studentId
     * @param oldPwd
     * @param newPwd
     * @return
     */
    @Caching(
            put = {
                    @CachePut(cacheNames = "user",key = "'getUserToCheckByPhone(' + #result.phone + ')'",unless = "#result == null"),
                    @CachePut(cacheNames = "user",key = "'getUserToCheckByStudentId(' + #studentId + ')'",unless = "#result == null")
            }
    )
    @Override
    public User updatePassword(String studentId,String oldPwd,String newPwd) {

        User user = getUserToCheckByStudentId(studentId);
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
                @CachePut(cacheNames = "user",key = "'getUserToCheckByPhone(' + #phone + ')'",unless = "#result == null"),
                @CachePut(cacheNames = "user",key = "'getUserToCheckByStudentId(' + #result.studentId + ')'",unless = "#result == null")
        }
    )
    public User updatePassword(String phone, String newPwd) {
        User user = getUserToCheckByPhone(phone);
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


    //后期使用布隆过滤器实现该方法
    
    /**
     * 检查手机号是否存在
     *
     * @param phone
     * @return
     */
    @Override
    @Cacheable(key = "#root.methodName + '(' + #root.args + ')'",unless = "#result == null")
    public boolean checkPhone(String phone) {
        User user = userMapper.getPhone(phone);
        if(user != null) {
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
    @Cacheable(key = "#root.methodName + '(' + #root.args + ')'",unless = "#result == null")
    public boolean checkStudentId(String studentId) {
        User user = userMapper.getStudentId(studentId);
        if(user != null) {
            return true;
        }
        return false;
    }


    public static void main(String[] args) {
        ByteSource credentialsSalt = ByteSource.Util.bytes("221701206");
        String newPassword = new SimpleHash("MD5", "123456abc",
                credentialsSalt, 1024).toBase64();
        System.out.println(newPassword);
    }
}
