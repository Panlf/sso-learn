package com.plf.learn.cookie.login.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.plf.learn.cookie.login.bean.User;
import com.plf.learn.cookie.login.utils.LoginCacheUtils;


@Controller
@RequestMapping("/view")
public class ViewController {

	@GetMapping("/login")
	public String toLogin(@RequestParam(required=false) String target,
			HttpSession session,@CookieValue(required=false,value="TOKEN") Cookie cookie){
		System.out.println("ViewController---"+target);
		if(StringUtils.isEmpty(target)){
			System.out.println("ViewController---target为空");
			target = "http://www.codeshop.com:9999";
		}
			
		if(cookie!=null){
			System.out.println("cookie不为空");
			String value = cookie.getValue();
			User user=LoginCacheUtils.loginUser.get(value);
			if(user != null){
				System.out.println("user不为空"+user);
				System.out.println("ViewController---"+target);
				return "redirect:"+target;
			}
		}
		
		session.setAttribute("target", target);
		
		return "login";
	}
}
