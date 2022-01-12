package com.plf.learn.utils;

import java.util.Random;

/**
 * 生成salt的静态方法
 * @author panlf
 * @date 2022/1/2
 */
public class SaltUtils {

    public static String getSalt(int n){
        char[] chars = "QWERTYUIOPASDFGHJKLZXCVBNM!@#$%^&*()1234567890".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i < n;i++){
            char c = chars[new Random().nextInt(chars.length)];
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    public static String getDefaultSalt() {
        return getSalt(8);
    }
}
