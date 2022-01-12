package com.plf.learn.realms;

import com.plf.learn.bean.Perms;
import com.plf.learn.bean.Role;
import com.plf.learn.bean.User;
import com.plf.learn.filter.JwtToken;
import com.plf.learn.service.UserService;
import com.plf.learn.utils.JwtUtil;
import com.plf.learn.utils.MySimpleByteSource;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author panlf
 * @date 2022/1/2
 */
@Component("customerRealm")
public class CustomerRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("————权限认证————");
        String username = JwtUtil.getUsername(principalCollection.toString());

       // String primaryPrincipal = (String) principalCollection.getPrimaryPrincipal();
        List<Role> roleList = userService.findRolesByUserName(username);
        if(!CollectionUtils.isEmpty(roleList)){
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            roleList.forEach(role -> {
                simpleAuthorizationInfo.addRole(role.getName());

                List<Perms> permsList = userService.findPermsByRoleId(role.getId());
                if(!CollectionUtils.isEmpty(permsList)){
                    permsList.forEach(
                        perms -> {
                            simpleAuthorizationInfo.addStringPermission(perms.getName());
                        }
                    );
                }
            });
            return simpleAuthorizationInfo;
        }
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("————身份认证方法————");
        //获得token
        String token =  (String)authenticationToken.getCredentials();
        //从token中获得username
        String username = JwtUtil.getUsername(token);
        //如果username为空或者验证不匹配
        if(username == null||!JwtUtil.verify(token,username)){
            throw new AuthenticationException("token认证失败!");
        }
        User user = userService.findByUserName(username);
        if(!ObjectUtils.isEmpty(user)){
           /*return new SimpleAuthenticationInfo(user.getUsername(),
                    user.getPassword(),
                    new MySimpleByteSource(user.getSalt()),
                    this.getName());*/
           return new SimpleAuthenticationInfo(token,token,"customerRealm");
        }
        throw new AuthenticationException("该用户不存在");
    }
}
