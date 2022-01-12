package com.plf.learn.filter;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author panlf
 * @date 2022/1/5
 */
public class JwtToken implements AuthenticationToken {

    private String token;

    public JwtToken(String token){
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
