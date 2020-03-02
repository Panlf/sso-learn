package com.plf.learn.cookie.login.controller;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

//import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plf.learn.cookie.login.bean.User;
import com.plf.learn.cookie.login.utils.CookieUtils;
import com.plf.learn.cookie.login.utils.LoginCacheUtils;

@Controller
@RequestMapping("/login")
public class LoginController {

	private static Set<User> dbUsers;
	static {
		dbUsers = new HashSet<>();
		dbUsers.add(new User(1,"zhangsan","123456"));
		dbUsers.add(new User(1,"lisi","123456"));
		dbUsers.add(new User(1,"wangwu","123456"));
	}
	@PostMapping
	public String doLogin(User user,HttpSession session,HttpServletRequest request,HttpServletResponse response){
		Optional<User> u = dbUsers.stream().filter(dbUser->dbUser.getUsername().equals(user.getUsername())
						&& dbUser.getPassword().equals(user.getPassword())).findFirst();
		if(u.isPresent()){
			String token=UUID.randomUUID().toString();
			/*Cookie cookie = new Cookie("TOKEN", token);
			cookie.setDomain("codeshop.com");
			response.addCookie(cookie);*/
			CookieUtils.setCookie(request, response, "TOKEN", token, 3600, false);
			LoginCacheUtils.loginUser.put(token, u.get());
		}else{
			session.setAttribute("msg", "用户名或者密码错误");
			return "login";
		}
		String target = (String) session.getAttribute("target");
		System.out.println("doLogin"+target);
		return "redirect:"+target;
	}
	
	@GetMapping("info")
	@ResponseBody
	public ResponseEntity<User> getUserInfo(String token){
		if(!StringUtils.isEmpty(token)){
			User user=LoginCacheUtils.loginUser.get(token);
			return ResponseEntity.ok(user);
		}
		return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
	}
	
}
