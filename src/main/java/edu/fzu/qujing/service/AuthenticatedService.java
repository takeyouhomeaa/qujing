package edu.fzu.qujing.service;

import edu.fzu.qujing.bean.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletResponse;

@Service
public interface AuthenticatedService {
    /**
     * 用户通过学号登录
     *
     * @param username 用户名
     * @param password  密码
     * @throws AuthenticationException 权限异常
     * @return
     */
    ResponseEntity<String> loginByStudentId(String username, String password, HttpServletResponse response) ;


    /**
     * 用户通过手机号登录
     *
     * @param phone
     * @param password
     * @param response
     * @return
     * @throws AuthenticationException
     */
    ResponseEntity<String> loginByPhone(String phone, String password, HttpServletResponse response) ;
    /**
     * 用户注册
     *
     * @param user 用户信息
     * @return
     */
    void register(User user);

    /**
     * 激活账户
     *
     * @param phone
     * @param check 用于检验是否合法
     * @return
     */
    boolean activeUser(String phone,String check);

    /**
     * 对验证码进行验证
     *
     * @param check
     * @return
     */

    boolean verifyCaptcha(String check,String phone);

    /**
     * 发送验证码
     *
     * @param phone
     * @return
     */
    ResponseEntity<String> sendCaptcha(String phone);

}
