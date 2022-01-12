package com.plf.learn.config;

import com.plf.learn.cache.RedisCacheManager;
import com.plf.learn.filter.JwtFilter;
import com.plf.learn.realms.CustomerRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 用来整合shiro框架相关的配置类
 * @author panlf
 * @date 2022/1/1
 */
@Configuration
public class ShiroConfig {


    //1.创建shiroFilter
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        //创建自定义过滤器
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        //将JWTFilter命名为jwt
        filterMap.put("jwt",new JwtFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
        //给filter设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);

        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized/NoPermission");
        shiroFilterFactoryBean.setLoginUrl("/noLogin");


        //配置系统受限资源
        //配置系统公共资源
        Map<String,String> map = new HashMap<>();
        //所有请求通过我们自己的过滤器

        map.put("/login","anon");
        map.put("/register","anon");
        map.put("/unauthorized/**","anon");
        //anon 匿名访问
        map.put("/getData","authc");// authc 请求这个资源需要认证和授权
        map.put("/**","jwt");
        //默认认证界面路径
        //shiroFilterFactoryBean.setLoginUrl("/login.jsp");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);

        return shiroFilterFactoryBean;
    }

    //2.创建安全管理器
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(CustomerRealm customerRealm){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();

        //给安全管理器设置
        defaultWebSecurityManager.setRealm(customerRealm);

        return defaultWebSecurityManager;
    }




    //3.创建自定义realm
    //@Bean
    public Realm realm(CustomerRealm customerRealm){
       // CustomerRealm customerRealm = new CustomerRealm();
       /*HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("MD5");
        credentialsMatcher.setHashIterations(1024);
        customerRealm.setCredentialsMatcher(credentialsMatcher);*/
        //开启缓存管理
        /*customerRealm.setCacheManager(new RedisCacheManager());
        customerRealm.setCachingEnabled(true); //开启全局缓存
        customerRealm.setAuthenticationCachingEnabled(true); //认证缓存
        customerRealm.setAuthenticationCacheName("authenticationCache");
        customerRealm.setAuthorizationCachingEnabled(true); //授权缓存
        customerRealm.setAuthorizationCacheName("authorizationCache");
        */
        return customerRealm;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager defaultWebSecurityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(defaultWebSecurityManager);
        return advisor;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

}
