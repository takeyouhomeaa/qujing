package edu.fzu.qujing.config.shiro;

import org.apache.shiro.authc.credential.*;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Configuration
public class ShiroConfig {


    @Bean
    public FilterRegistrationBean<Filter> registrationBean(@Qualifier("jwtFilter") JwtFilter filter) {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter();
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("securityManager") SecurityManager securityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager);
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("jwtFilter", jwtFilter());
        //filterMap.put("authc",new SimpleFormAuthenticationFilter()); 暂定
        bean.setFilters(filterMap);

        Map<String,String> map = new LinkedHashMap<>();
        map.put("/authenticated/**","anon");
        map.put("/swagger-ui.html","anon");
        map.put("/swagger/**", "anon");
        map.put("/swagger-resources/**", "anon");
        map.put("/v2/**", "anon");
        map.put("/webjars/**", "anon");
        map.put("/configuration/**", "anon");
        map.put("/authenticated/logout", "logout");
        map.put("/**","jwtFilter,authc");
        bean.setFilterChainDefinitionMap(map);

        return bean;
    }


    @Bean
    public ModularRealmAuthenticator authenticator() {
        ModularRealmAuthenticator authenticator = new MultiRealmAuthenticator();
        AuthenticationStrategy strategy = new FirstSuccessfulStrategy();
        authenticator.setAuthenticationStrategy(strategy);
        return authenticator;
    }

    @Bean
    protected SessionStorageEvaluator sessionStorageEvaluator() {
        DefaultSessionStorageEvaluator sessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        return sessionStorageEvaluator;
    }


    @Bean("securityManager")
    @DependsOn("hashedCredentialsMatcher")
    public SecurityManager securityManager(@Qualifier("userRealm") UserRealm userRealm,
                                           @Qualifier("phoneRealm") PhoneRealm phoneRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        securityManager.setAuthenticator(authenticator());

        List<Realm> list = new ArrayList<>();
        list.add(userRealm);
        list.add(jwtRealm());
        list.add(phoneRealm);
        securityManager.setRealms(list);

        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        subjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator());
        securityManager.setSubjectDAO(subjectDAO);

        return securityManager;
    }

    @Bean
    public JwtRealm jwtRealm() {
        JwtRealm jwtRealm = new JwtRealm();
        CredentialsMatcher credentialsMatcher = new JwtCredentialsMatcher();
        jwtRealm.setCredentialsMatcher(credentialsMatcher);
        return jwtRealm;
    }

    @Bean("userRealm")
    public UserRealm userRealm(@Qualifier("hashedCredentialsMatcher") HashedCredentialsMatcher credentialsMatcher) {
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(credentialsMatcher);
        return userRealm;
    }

    @Bean("phoneRealm")
    public PhoneRealm phoneRealm(@Qualifier("simpleCredentialsMatcher") SimpleCredentialsMatcher credentialsMatcher) {
        PhoneRealm phoneRealm = new PhoneRealm();
        phoneRealm.setCredentialsMatcher(credentialsMatcher);
        return phoneRealm;
    }

    @Bean("hashedCredentialsMatcher")
    public HashedCredentialsMatcher credentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("MD5");
        credentialsMatcher.setHashIterations(1024);
        credentialsMatcher.setStoredCredentialsHexEncoded(false);
        return credentialsMatcher;
    }


    @Bean("simpleCredentialsMatcher")
    public SimpleCredentialsMatcher simpleCredentialsMatcher() {
        SimpleCredentialsMatcher credentialsMatcher = new SimpleCredentialsMatcher();
        return credentialsMatcher;
    }
}
