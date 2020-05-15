package edu.fzu.qujing.config.shiro;

import edu.fzu.qujing.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

@Slf4j
public class JwtCredentialsMatcher implements CredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        String studentId = (String) authenticationToken.getPrincipal();
        String token = (String) authenticationInfo.getCredentials();
        try {
            Claims claims = JwtUtil.parseJwt(token);
            String subject = claims.getSubject();
            if (studentId.equals(subject)) {
                throw new AuthenticationException("Unauthorized");
            }
            if (!JwtUtil.isTokenExpired(claims.getExpiration())) {
                throw new AuthenticationException("Authentication expired, please login again");
            }
            return true;
        } catch (Exception e) {
            log.error("Exception in authentication process {}",e.getMessage());
            return false;
        }
    }
}
