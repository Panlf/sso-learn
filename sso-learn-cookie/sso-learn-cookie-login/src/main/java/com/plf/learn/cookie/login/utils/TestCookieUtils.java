package com.plf.learn.cookie.login.utils;

public class TestCookieUtils {

	public static void main(String[] args) {
		String domainName = null;
		
		String serverName = "http://cart.codeshop.com/";
		
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
				domainName = "."+domains[len-3]+"."+domains[len-2]+"."+domains[len-1];
			}else if(len <=3 && len > 1){
				domainName = "."+domains[len-2]+"."+domains[len-1];
			}else{
				domainName = serverName;
			}
			
		}

		if(domainName != null && domainName.indexOf(":") > 0){
			String[] ary = domainName.split("\\:");
			domainName = ary[0];
		}
		
		System.out.println(domainName);
	}
}
