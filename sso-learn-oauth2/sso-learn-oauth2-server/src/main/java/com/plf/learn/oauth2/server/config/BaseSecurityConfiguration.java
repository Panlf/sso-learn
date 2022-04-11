package com.plf.learn.oauth2.server.config;

import com.plf.learn.oauth2.server.service.AuthUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

/**
 * @author Panlf
 * @date 2020/3/16
 */
@Configuration
@EnableWebSecurity
public class BaseSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Resource
    private AuthUserDetailsService authUserDetailsService;

    @Resource
    private PasswordEncoder passwordEncoder;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // 使用自定义认证与授权
        auth.userDetailsService(authUserDetailsService).passwordEncoder(passwordEncoder);

        //硬编码
		/*auth.inMemoryAuthentication()
			.withUser("admin")
			.password(passwordEncoder().encode("123"))
			.roles("ADMIN");*/
    }

    /**
     * 认证管理器
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 将 check_token 暴露出去，否则资源服务器访问时报 403 错误
        web.ignoring().antMatchers("/oauth/check_token");
    }
}
