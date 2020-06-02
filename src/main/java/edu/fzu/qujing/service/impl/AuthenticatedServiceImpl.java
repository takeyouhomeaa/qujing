package edu.fzu.qujing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.fzu.qujing.bean.User;
import edu.fzu.qujing.config.shiro.PhoneToken;
import edu.fzu.qujing.mapper.UserMapper;
import edu.fzu.qujing.service.AuthenticatedService;
import edu.fzu.qujing.service.PageService;
import edu.fzu.qujing.service.UserService;
import edu.fzu.qujing.util.*;
import io.jsonwebtoken.Claims;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Service
@Transactional(rollbackFor = Exception.class)
public class AuthenticatedServiceImpl implements AuthenticatedService {

    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    ThreadPoolTaskExecutor executor;

    @Resource
    PageService taskPageServiceImpl;

    @Resource
    PageService settlePageServiceImpl;


    private ResponseEntity<String> checkAccountStatus(User userToCheck) {
        if(userToCheck == null) {
            return ResponseEntity.status(404).body("Account does not exist");
        }else {
            Date nowTime = new Date();
            Date endTime = userToCheck.getEndTime();
            if (endTime != null && nowTime.compareTo(endTime) != -1) {
                userService.updateState(userToCheck.getStudentId(),1);
            }else if(endTime != null && nowTime.compareTo(endTime) == -1){
                userService.updateState(userToCheck.getStudentId(),2);
                return ResponseEntity.status(403).body("Account is frozen to " + userToCheck.getEndTime());
            }

        }

        return null;
    }

    private ResponseEntity<String> getStringResponseEntity(HttpServletResponse response, Subject subject, AuthenticationToken token, User userToCheck) {
        ResponseEntity<String> status = checkAccountStatus(userToCheck);
        if(status != null ) {
            return status;
        }

        subject.login(token);

        String jwtToken = JwtUtil.creatJwt(JwtUtil.JWT_ID,userToCheck.getStudentId(),JwtUtil.JWT_EXPIRE);
        System.out.println(jwtToken);
        response.setHeader(JwtUtil.AUTH_HEADER,jwtToken);
        executor.execute(taskPageServiceImpl.cachePreload(userToCheck.getStudentId()));
        executor.execute(settlePageServiceImpl.cachePreload(userToCheck.getStudentId()));
        return ResponseEntity.ok("success");
    }

    /**
     * 用户登录
     *
     * @param username 账号
     * @param password 密码
     * @throws AuthenticationException 权限异常
     */
    @Override
    public ResponseEntity<String> loginByStudentId(String username, String password,
                                        HttpServletResponse response){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token  = new UsernamePasswordToken(username,password);
        User userToCheck = userService.getUserToCheckByStudentId(username);
        return getStringResponseEntity(response, subject, token, userToCheck);
    }


    @Override
    public ResponseEntity<String> loginByPhone(String phone, String password, HttpServletResponse response) {
        Subject subject = SecurityUtils.getSubject();
        PhoneToken token  = new PhoneToken(phone,password);
        User userToCheck = userService.getUserToCheckByPhone(phone);
        return getStringResponseEntity(response, subject, token, userToCheck);
    }

    /**
     * 激活账户
     *
     * @param check 用于检验是否合法
     * @return
     */
    @Override
    public boolean activeUser(String phone,String check) {
        String key = "register::" + phone;
        String key2 = "toBeActivated::" + phone;
        if(RedisUtil.hasKey(key)) {
            Object rs = RedisUtil.get(key);
            if (rs.equals(check)) {
                Object obj = RedisUtil.get(key2);
                User user = (User)obj;
                userService.save(user);
                return true;
            }
        }
        return false;
    }


    /**
     * 用户注册
     *
     * @param user 用户信息
     */
    @Override
    public void register(User user) {
        userService.saveUser(user);
    }

    @Override
    public ResponseEntity<String> sendCaptcha(String phone,String scenes) {
        String key = scenes + "::" + phone;
        String code = PhoneUtil.getCode();
        System.out.println(code);
        boolean flag = PhoneUtil.send(phone, code);
        if(flag) {
            RedisUtil.set(key, code,60 * 5);
        }else {
            throw new RuntimeException("SMS server is busy");
        }
        return ResponseEntity.ok("Captcha has been sent");
    }

    @Override
    public void logout(String studentId){}


    @Override
    public boolean verifyCaptchaToRegister(String check,String phone) {
        String key = "register::" + phone;
        return verifyCaptcha(check, key);
    }

    @Override
    public boolean verifyCaptchaToForgetPwd(String check, String phone) {
        String key = "forgetPwd::" + phone;
        RedisUtil.set("forgetPwd::flag", "true",120);
        return verifyCaptcha(check, key);
    }

    @Override
    public boolean verifyCaptchaToLogin(String check, String phone) {
        String key = "login::" + phone;
        return verifyCaptcha(check, key);
    }

    private boolean verifyCaptcha(String check, String key) {
        if(RedisUtil.hasKey(key)){
            Object rs = RedisUtil.get(key);
            String code = (String)rs;
            if(code.equals(check)){
                RedisUtil.del(key);
                return true;
            }
        }
        return false;
    }
}
