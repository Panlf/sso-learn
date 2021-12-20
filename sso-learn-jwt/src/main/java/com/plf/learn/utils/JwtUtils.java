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
     * @param map
     * @return
     */
    public static String getToken(Map<String,String> map){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND,90); //默认30分钟

        JWTCreator.Builder builder = JWT.create();

        map.forEach((k,v) -> {
            builder.withClaim(k,v);
        });

        String token = builder.withExpiresAt(calendar.getTime())
                .sign(Algorithm.HMAC256(SIGN));

        return token;
    }

    /**
     * 验证token
     * @param token
     */
    public static void verify(String token){
        JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
    }
}
