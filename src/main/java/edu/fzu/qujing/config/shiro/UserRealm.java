package edu.fzu.qujing.config.shiro;

import edu.fzu.qujing.bean.User;
import edu.fzu.qujing.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import javax.annotation.Resource;
import java.util.Date;

public class UserRealm extends AuthorizingRealm {

    @Lazy
    @Resource
    UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        User userToCheck  = userService.getUserToCheckByStudentId(username);

        ByteSource credentialsSalt = ByteSource.Util.bytes(userToCheck.getStudentId());
        System.out.println("realm :" + userToCheck);

        return new SimpleAuthenticationInfo(userToCheck.getStudentId(),
                userToCheck.getPassword(),
                credentialsSalt,
                getName());

    }

}
