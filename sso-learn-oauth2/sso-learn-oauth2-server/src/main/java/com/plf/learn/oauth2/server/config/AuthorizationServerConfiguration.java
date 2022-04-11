package com.plf.learn.oauth2.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Arrays;

/**
 * @EnableAuthorizationServer注解开启OAuth2授权服务机制
 *
 * @author Panlf
 * @date 2020/3/16
 */
@Configuration
@EnableAuthorizationServer
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true,jsr250Enabled = true)
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

	//@Autowired
	//private JwtAccessTokenConverter accessTokenConverter;

    //@Autowired
    //private PasswordEncoder passwordEncoder;

	//@Autowired
	//public TokenStore tokenStore;

	@Resource
	private ClientDetailsService clientDetailsService;

	@Resource
	private AuthorizationCodeServices authorizationCodeServices;

	@Resource
	private AuthenticationManager authenticationManager;

	private String SIGNING_KEY = "123";

	@Resource
	private RedisConnectionFactory redisConnectionFactory;

	/**
	 * 配置MySql数据源
	 * @return
	 */
	@Primary
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
		// 配置数据源
		return DataSourceBuilder.create().build();
	}


	@Bean
	public ClientDetailsService jdbcClientDetailsService() {
		// 基于 JDBC 实现，需要事先在数据库配置客户端信息
		return new JdbcClientDetailsService(dataSource());
	}

	@Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		//读取客户端配置
		clients.withClientDetails(jdbcClientDetailsService());


		//配置客户端详细信息服务
        //硬编码
		/*clients
			//使用内存模式
			.inMemory()
                //client_id
			.withClient("client")
                ////客户端秘钥 client_secret
			.secret(passwordEncoder.encode("secret"))
                //资源列表
			.resourceIds("resource1")
                //该client允许的授权类型
			.authorizedGrantTypes("authorization_code","password",
					"client_credentials","implicit","refresh_token")
                //允许的授权范围 比如读取
			.scopes("app","pc")
                //false 跳转到授权页面
			.autoApprove(false)
			    //验证的回调地址
			.redirectUris("https://www.baidu.com");*/
    }


	/**
	 * 令牌管理模式
	 * @return
	 */
	@Bean
	public AuthorizationServerTokenServices tokenServices(){
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setClientDetailsService(clientDetailsService);
		defaultTokenServices.setSupportRefreshToken(true);
		defaultTokenServices.setTokenStore(tokenStore());
		// 令牌增强
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter()));
		defaultTokenServices.setTokenEnhancer(tokenEnhancerChain);

		// 两小时
		defaultTokenServices.setAccessTokenValiditySeconds(7200);
		// 3天
		defaultTokenServices.setRefreshTokenValiditySeconds(259200);

		//是否支持refresh_token，默认false
		defaultTokenServices.setSupportRefreshToken(true);
		//是否复用refresh_token,默认为true(如果为false,则每次请求刷新都会删除旧的refresh_token,创建新的refresh_token)
		defaultTokenServices.setReuseRefreshToken(true);

		return defaultTokenServices;
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		// 设置令牌
		endpoints
				.authenticationManager(authenticationManager)
				.authorizationCodeServices(authorizationCodeServices)
				.tokenServices(tokenServices())
				.allowedTokenEndpointRequestMethods(HttpMethod.POST);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.
				//oauth/token_key是公开的
				tokenKeyAccess("permitAll()")
				//oauth/check_token是公开的
				.checkTokenAccess("permitAll()")
				//表单认证
				.allowFormAuthenticationForClients();

	}


	/**
	 * 使用jdbc进行维护code，将code存储在表oauth_code
	 * @return
	 */
	@Bean
	public AuthorizationCodeServices authorizationCodeServices(){
		//code可以放在JDBC中，也可以放在Redis中
		//return new InMemoryAuthorizationCodeServices();
		return new JdbcAuthorizationCodeServices(dataSource());
	}


	/**
	 * @Description 自定义token令牌增强器
	 * @Date 2019/7/11 16:22
	 * @Version  1.0
	 */
	@Bean
	public JwtAccessTokenConverter accessTokenConverter(){
		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
		accessTokenConverter.setSigningKey(SIGNING_KEY);
		return accessTokenConverter;
	}

	/**
	 * RedisTokenStore使用Redis持久化token
	 * @Description JwtTokenStore OAuth2 token持久化接口
	 * @Date 2019/7/9 17:45
	 * @Version  1.0
	 */
	@Bean
	public TokenStore tokenStore() {
		//return new JwtTokenStore(accessTokenConverter());
		//return new JdbcTokenStore(dataSource());
		return new RedisTokenStore(redisConnectionFactory);
	}
}
