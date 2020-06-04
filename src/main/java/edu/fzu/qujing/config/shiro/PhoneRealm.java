package edu.fzu.qujing.config.shiro;

import edu.fzu.qujing.bean.User;
import edu.fzu.qujing.service.UserService;
import edu.fzu.qujing.util.RedisUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

public class PhoneRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof PhoneToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        PhoneToken token = (PhoneToken) authenticationToken;
        Object principal = token.getPrincipal();
        String phone = (String) principal;
        String key = "login::" + phone;
        if(RedisUtil.hasKey(key)) {
            Object obj = RedisUtil.get("login::" + phone);
            String code = (String)obj;
            return new SimpleAuthenticationInfo(phone, code,getName());
        }

        return null;

    }
}
