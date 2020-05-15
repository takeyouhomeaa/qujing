package edu.fzu.qujing.config.shiro;

import edu.fzu.qujing.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationToken;


@Slf4j
public class JwtToken implements AuthenticationToken {

    private String token;
    private String userName;

    public JwtToken(String token) {
        this.token = token;
        this.userName = getClaims(token);
    }
    @Override
    public Object getPrincipal() {
        return this.userName;
    }

    @Override
    public Object getCredentials() {
        return this.token;
    }

    private String getClaims(String token){
        try {
            Claims claims = JwtUtil.parseJwt(token);
            return claims.getSubject();
        } catch (Exception e) {
            log.error("Exception in token parsing");
            return null;
        }
    }
}
