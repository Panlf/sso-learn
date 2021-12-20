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
        try{
            //验证令牌
            JwtUtils.verify(token);
        }catch (SignatureVerificationException e){
            return e.getMessage();
        }catch (TokenExpiredException e){
            return e.getMessage();
        }catch (AlgorithmMismatchException e){
            return e.getMessage();
        }catch (Exception e){
            return e.getMessage();
        }
        return "token验证通过";
    }

}
