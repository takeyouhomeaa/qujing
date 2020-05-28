package edu.fzu.qujing.config.shiro;

import org.apache.shiro.authc.AuthenticationToken;

public class PhoneToken implements AuthenticationToken {

    private String phone;
    private String check;

    public PhoneToken(String phone, String check) {
        this.phone = phone;
        this.check = check;
    }

    @Override
    public Object getPrincipal() {
        return this.phone;
    }

    @Override
    public Object getCredentials() {
        return this.check;
    }
}
