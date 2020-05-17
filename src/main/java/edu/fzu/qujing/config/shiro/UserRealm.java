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
        User userToCheck;
        if (username.indexOf("@") == -1) {
            userToCheck = userService.getUserToCheckByStudentId(username);
        }else {
            userToCheck = userService.getUserToCheckByEmail(username);
        }
        if(userToCheck == null){
            throw new UnknownAccountException();
        }
        if (userToCheck != null) {
            Date nowTime = new Date();
            Date endTime = userToCheck.getEndTime();
            if (endTime != null && nowTime.compareTo(endTime) != -1) {
                userService.updateState(userToCheck.getStudentId(),1);
            }else if(endTime != null && nowTime.compareTo(endTime) == -1){
                userService.updateState(userToCheck.getStudentId(),2);
                throw new  LockedAccountException();
            }

            if (userToCheck.getState() == 0) {
                throw new DisabledAccountException();
            }

            if(userToCheck.getState() == 1){
                ByteSource credentialsSalt = ByteSource.Util.bytes(userToCheck.getStudentId());
                return new SimpleAuthenticationInfo(userToCheck.getStudentId(),
                                                    userToCheck.getPassword(),
                                                    credentialsSalt,
                                                    getName());
            }
        }
        return null;
    }

}
