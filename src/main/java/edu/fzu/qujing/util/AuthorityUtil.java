package edu.fzu.qujing.util;

import org.springframework.core.NamedThreadLocal;

import java.util.Date;

public class AuthorityUtil {
    private static final ThreadLocal<String> principal = new NamedThreadLocal<>("principal");

    public static void setPrincipal(String principal) {
        AuthorityUtil.principal.set(principal);
    }

    public static String getPrincipal() {
        return AuthorityUtil.principal.get();
    }

    public static void removePrincipal() {
        principal.remove();
    }
}
