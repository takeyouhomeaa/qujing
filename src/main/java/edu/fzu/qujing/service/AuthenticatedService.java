package edu.fzu.qujing.service;

import edu.fzu.qujing.bean.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletResponse;

@Service
public interface AuthenticatedService {
    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password  密码
     * @throws AuthenticationException 权限异常
     * @return
     */
    public ResponseEntity<String> login(String username, String password
            ,HttpServletResponse response) throws AuthenticationException;

    /**
     * 用户注册
     *
     * @param user 用户信息
     */
    public void register(User user);

    /**
     * 激活账户
     *
     * @param check 用于检验是否合法
     * @return
     */
    public boolean activeUser(String check);

}
