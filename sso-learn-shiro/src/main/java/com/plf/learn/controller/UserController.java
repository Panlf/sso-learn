package com.plf.learn.controller;

import com.plf.learn.bean.User;
import com.plf.learn.service.UserService;
import com.plf.learn.utils.JwtUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * @author panlf
 * @date 2022/1/2
 */
@RestController
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping("register")
    public String register(User user){
        try {
            userService.register(user);
            return "注册成功";
        }catch (Exception e){
            e.printStackTrace();
            return "注册失败";
        }
    }

    /**
     * 退出登录
     * @return 返回提示
     */
    @RequestMapping("logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "退出登录成功";
    }

    @RequestMapping("getDataAdmin")
    @RequiresRoles("admin")
    public String getDataAdmin(){
        return "给你数据";
    }

    @RequestMapping("getDataUser")
    @RequiresRoles("user")
    public String getDataUser(){
        return "给你数据";
    }

    @RequestMapping("noLogin")
    public String noLogin(){
        return "您还未登录，请登录一下~";
    }

    @RequestMapping("unauthorized/{message}")
    public String unAuthorized(@PathVariable String message){
        return "您没有权限操作!!!"+message;
    }


    @RequestMapping("login")
    public String login(String username,String password){
        // 获取主题对象
        //Subject subject = SecurityUtils.getSubject();
        try {
           // subject.login(new UsernamePasswordToken(username,password));
            return JwtUtil.createToken(username);
        } catch (UnknownAccountException e){
            e.printStackTrace();
            return "用户名错误";
        } catch (IncorrectCredentialsException e){
            e.printStackTrace();
            return "密码错误";
        }
    }

}
