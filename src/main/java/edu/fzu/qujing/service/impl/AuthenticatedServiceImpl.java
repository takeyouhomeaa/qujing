package edu.fzu.qujing.service.impl;

import edu.fzu.qujing.bean.User;
import edu.fzu.qujing.service.AuthenticatedService;
import edu.fzu.qujing.service.UserService;
import edu.fzu.qujing.util.JwtUtil;
import edu.fzu.qujing.util.MailUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

@Service
@Transactional(rollbackFor = Exception.class)
public class AuthenticatedServiceImpl implements AuthenticatedService {

    @Autowired
    UserService userService;

    /**
     * 用户登录
     *
     * @param username 账号
     * @param password 密码
     * @throws AuthenticationException 权限异常
     */
    @Override
    public ResponseEntity<String> login(String username, String password,
                                        HttpServletResponse response) throws AuthenticationException{
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token  = new UsernamePasswordToken(username,password);
        User user = new User();
        if (username.indexOf("@") == -1) {
            user.setStudentId(username);
        }else {
            user.setEmail(username);
        }
        User userToCheck = userService.getUserToCheck(user);
        try {
            subject.login(token);
        }catch (UnknownAccountException e) {
            return ResponseEntity.status(401).body("Username or password incorrect");
        }catch (LockedAccountException e) {
            return ResponseEntity.status(403).body("Account is frozen to " + userToCheck.getEndTime());
        }catch (DisabledAccountException e) {
            return ResponseEntity.status(404).body("Account does not exist");
        }
        String jwtToken = JwtUtil.creatJwt("ozg",userToCheck.getStudentId(),1000*60*30);
        response.setHeader("Authorization",jwtToken);
        return ResponseEntity.ok("success");
    }

    /**
     * 激活账户
     *
     * @param id    user的id
     * @param check 用于检验是否合法
     */
    @Override
    public boolean activeUser(Integer id, String check) {
        ByteSource credentialsSalt = ByteSource.Util.bytes(id.toString());
        String rs = new SimpleHash("MD5",id.toString(),credentialsSalt,1024).toBase64();
        if(rs.equals(check)){
            return true;
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
        Integer id = userService.saveUser(user);
        ByteSource credentialsSalt = ByteSource.Util.bytes(id.toString());
        String encode = new SimpleHash("MD5",id.toString(),credentialsSalt,1024).toBase64();
        MailUtil.sendToNoSSL(user.getEmail(),id,user.getUsername(),encode);
    }
}
