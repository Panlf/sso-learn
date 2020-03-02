package com.plf.learn.cookie.login.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

	public static final void setCookie(HttpServletRequest request,HttpServletResponse response,String cookieName,
			String cookieValue,int cookieMaxage,String encodeString){
		doSetCookie(request,response,cookieName,cookieValue,cookieMaxage,encodeString);
	}
	
	public static final void setCookie(HttpServletRequest request,HttpServletResponse response,String cookieName,
			String cookieValue,int cookieMaxage,boolean isEncode){
		doSetCookie(request,response,cookieName,cookieValue,cookieMaxage,isEncode);
	}
	
	private static final void doSetCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
			String cookieValue,int cookieMaxage, String encodeString) {
		
		try {
			
			if(cookieValue == null){
				cookieValue ="";
			}else{
				cookieValue = URLEncoder.encode(cookieValue,encodeString);
			}
			
			Cookie cookie = new Cookie(cookieName,cookieValue);
			
			if(cookieMaxage > 0){
				cookie.setMaxAge(cookieMaxage);
			}
			if(null != request){//设置域名的cookie
				String domainName = getDomainName(request);
				if(!"localhost".equals(domainName)){
					cookie.setDomain(domainName);
				}
			}
			cookie.setPath("/");
			response.addCookie(cookie);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static final void doSetCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
			String cookieValue,int cookieMaxage, boolean isEncode) {
		try {
			
			if(cookieValue == null){
				cookieValue ="";
			}else if(isEncode){
				cookieValue = URLEncoder.encode(cookieValue,"UTF-8");
			}
			
			Cookie cookie = new Cookie(cookieName,cookieValue);
			
			if(cookieMaxage > 0){
				cookie.setMaxAge(cookieMaxage);
			}
			if(null != request){//设置域名的cookie
				//这里是session的跨域共享的条件
				//www.test.com  sso.test.com  返会的是test.com  只要是以test.com就以同一个跨域共享
				String domainName = getDomainName(request);
				if(!"localhost".equals(domainName)){
					cookie.setDomain(domainName);
				}
			}
			cookie.setPath("/");
			response.addCookie(cookie);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private static String getDomainName(HttpServletRequest request) {
		String domainName = null;
		
		//请求完整的请求URL地址
		String serverName = request.getRequestURL().toString();
		
		if(serverName == null || serverName.equals("")){
			domainName = "";
		}else{
			serverName = serverName.toLowerCase();
			
			if(serverName.startsWith("http://")){
				serverName = serverName.substring(7);
			}else if(serverName.startsWith("https://")){
				serverName = serverName.substring(8);
			}
			
			final int end = serverName.indexOf("/");
			serverName = serverName.substring(0, end);
			final String[] domains = serverName.split("\\.");
			int len = domains.length;
			if(len > 3){
				domainName = domains[len-3]+"."+domains[len-2]+"."+domains[len-1];
			}else if(len <=3 && len > 1){
				domainName = domains[len-2]+"."+domains[len-1];
			}else{
				domainName = serverName;
			}
			
		}

		if(domainName != null && domainName.indexOf(":") > 0){
			String[] ary = domainName.split("\\:");
			domainName = ary[0];
		}
		
		return domainName;	
	}

	public static String getCookieValue(HttpServletRequest request, String cookieName, String encodeString) {
		Cookie[] cookieList = request.getCookies();
		
		if(cookieList == null || cookieName == null){
			return null;
		}
		
		String retValue = null;
		
		try {
			for(int i=0;i<cookieList.length;i++){
				if(cookieList[i].getName().equals(cookieName)){
					retValue = URLDecoder.decode(cookieList[i].getValue(),encodeString);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return retValue;
	}
}
