package com.plf.learn.oauth2.resource.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;

/**
 * @author Panlf
 * @date 2020/3/16
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    public static final String RESOURCE_ID = "resource1";

    //@Autowired
    //TokenStore tokenStore;

    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID)
                .tokenStore(tokenStore())
                .stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        //hasAnyAuthority 与 hasAuthority
        //设置权限
        http.authorizeRequests()
                .antMatchers("/resource/app/**").access("#oauth2.hasScope('app')")
                .antMatchers("/resource/pc/**").access("#oauth2.hasScope('pc')");
        http.authorizeRequests()
                .antMatchers("/opt/insert").hasAuthority("RESOURCE_INSERT")
                .and().authorizeRequests().antMatchers("/opt/update").hasAuthority("RESOURCE_UPDATE")
                .and().authorizeRequests().antMatchers("/opt/delete").hasAuthority("RESOURCE_DELETE")
                .and().authorizeRequests().antMatchers("/opt/select").hasAuthority("RESOURCE_SELECT")
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }

    @Bean
    public TokenStore tokenStore() {
        //return new JwtTokenStore(accessTokenConverter());
        //return new JdbcTokenStore(dataSource());
        return new RedisTokenStore(redisConnectionFactory);
    }
}

