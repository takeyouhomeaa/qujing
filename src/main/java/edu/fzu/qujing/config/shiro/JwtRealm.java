package edu.fzu.qujing.config.shiro;

import edu.fzu.qujing.bean.User;
import edu.fzu.qujing.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class JwtRealm extends AuthorizingRealm {
    @Autowired
    UserService userService;
    
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        JwtToken jwtToken = (JwtToken)authenticationToken;
        if(jwtToken.getPrincipal() == null) {
            throw new AccountException("JWT token parameter exception");
        }

        String username = jwtToken.getPrincipal().toString();
        User user = new User();
        user.setStudentId(username);
        User userToCheck = userService.getUserToCheck(user);
        if(userToCheck == null) {
            throw new UnknownAccountException("User does not exist");
        }
        if(userToCheck.getState() == 2) {
            throw new  LockedAccountException("Account is frozen");
        }

        if(userToCheck.getState() == 1) {
            return new SimpleAuthenticationInfo(userToCheck.getStudentId(),
                    jwtToken.getCredentials(),
                    getName());
        }

        return null;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }
}
