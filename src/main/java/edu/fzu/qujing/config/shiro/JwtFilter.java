package edu.fzu.qujing.config.shiro;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.fzu.qujing.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

    /**
     * 过滤器拦截请求的入口方法
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return 返回 true 则允许访问 返回false 则禁止访问，会进入 onAccessDenied()
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if(isLoginAttempt(request,response)) {
            return false;
        }
        boolean allowed = false;
        try {
            allowed = executeLogin(request, response);
        }catch (IllegalStateException e) {
            log.error("Not found any token");
        } catch (Exception e) {
            log.error("Error occurs when login", e);
        }
        return allowed || super.isPermissive(mappedValue);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpServletResponse= (HttpServletResponse)request;
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code","401");
        jsonObject.put("msg","UNAUTHORIZED");
        String json = JSON.toJSONString(jsonObject);
        httpServletResponse.getWriter().write(json);
        return false;
    }

    /**
     * 检测Header中是否包含 JWT token 字段
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        return httpServletRequest.getAttribute(JwtUtil.AUTH_HEADER) == null;
    }

    /**
     * 从head中提取jwtToken
     * @param request
     * @param response
     * @return
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = (String) httpServletRequest.getAttribute(JwtUtil.AUTH_HEADER);
        JwtToken token = new JwtToken(authorization);
        return token;
    }


    /**
     * 身份验证,检查 JWT token 是否合法
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        AuthenticationToken token = createToken(request, response);
        if(token == null) {
            String msg = "createToken method implementation returned null." +
                    " A valid non-null AuthenticationToken "
                    + "must be created in order to execute a login attempt.";
            throw new IllegalStateException(msg);
        }
        try {
            Subject subject = getSubject(request, response);
            subject.login(token);
            return onLoginSuccess(token, subject, request, response);
        }catch (AuthenticationException e) {
            return onLoginFailure(token, e, request, response);
        }
    }

    /**
     * Shiro 利用 JWT token 登录成功，会进入该方法
     * @param token
     * @param subject
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;
        String newToken = null;
        if(token instanceof JwtToken){
            newToken = JwtUtil.creatJwt(JwtUtil.JWT_ID,token.getCredentials().toString(),JwtUtil.JWT_EXPIRE);
        }
        if (newToken != null) {
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setHeader(JwtUtil.AUTH_HEADER, newToken);
        }
        return true;
    }

    /**
     * Shiro 利用 JWT token 登录失败，会进入该方法
     * @param token
     * @param e
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        return super.onLoginFailure(token, e, request, response);
    }
}
