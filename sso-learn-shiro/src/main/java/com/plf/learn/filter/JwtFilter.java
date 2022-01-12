package com.plf.learn.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author panlf
 * @date 2022/1/5
 */
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        //判断请求头是否带上“Token”
        if(isLoginAttempt(request, response)){
            //如果存在，则执行executeLogin方法登入，检查token是否正确
            try {
                executeLogin(request, response);
                return true;
            } catch (Exception e) {
                responseError(response,e.getMessage());
            }
        }
        //如果没有token，则可能是执行登录操作或者是游客状态访问，无需检查token，直接返回true
        return true;
    }

    /**
     * 将非法请求跳转到 /unauthorized/**
     * @param response
     * @param message
     */
    private void responseError(ServletResponse response, String message) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        try {
            message = URLEncoder.encode(message, StandardCharsets.UTF_8);
            httpServletResponse.sendRedirect("/unauthorized/"+message);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 判断用户是否想要登入
     * 检测header里面是否包含Token字段
     * @param request request
     * @param response response
     * @return boolean
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Token");
        return token != null;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader("Token");
        JwtToken jwtToken = new JwtToken(token);
        System.out.println(jwtToken);
        //提交给realm进行登入，如果错误就会抛出异常并被捕获
        getSubject(request, response).login(jwtToken);
        return true;
    }

    /**
     * 对跨域访问提供支持
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
}
