package com.plf.learn.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Calendar;
import java.util.Map;

/**
 * @author panlf
 * @date 2021/12/20
 */
public class JwtUtils {
    private static final String SIGN = "BA!@#CP";

    /**
     * 生成token
     * @param map payload信息
     * @return 返回token
     */
    public static String getToken(Map<String,String> map){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND,90); //默认90秒

        JWTCreator.Builder builder = JWT.create();

        map.forEach(builder::withClaim);

        return builder.withExpiresAt(calendar.getTime())
                .sign(Algorithm.HMAC256(SIGN));
    }

    /**
     * 验证token
     * @param token 需验证的token
     */
    public static void verify(String token){
        JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
    }
}
