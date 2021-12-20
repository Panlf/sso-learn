package com.plf.learn.controller;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.plf.learn.utils.JwtUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author panlf
 * @date 2021/12/20
 */
@RestController
@RequestMapping("jwt")
public class JWTController {


    @GetMapping("/generate")
    public String generateToken(String username){
        Map<String,String> map = new HashMap<>();
        map.put("username",username);
        return JwtUtils.getToken(map);
    }

    @GetMapping("/verify")
    public String verifyToken(String token){
        String result =  "token验证通过";
        try{
            //验证令牌
            JwtUtils.verify(token);
        }catch (SignatureVerificationException e){
            result = "token签名不一致";
        }catch (TokenExpiredException e){
            result = "token过期";
        }catch (AlgorithmMismatchException e){
            result = "token算法不匹配";
        }catch (Exception e){
            result = e.getMessage();
        }
        return result;
    }

}
