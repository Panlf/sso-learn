package com.plf.learn.oauth2.resource.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Panlf
 * @date 2020/3/16
 */
@RestController
@RequestMapping("/opt/")
public class OptController {


    @GetMapping("/insert")
    public String insertResource(){
        return "insert资源";
    }

    @GetMapping("/update")
    public String updateResource(){
        return "update资源";
    }

    @GetMapping("/delete")
    public String deleteResource(){
        return "delete资源";
    }

    @GetMapping("/select")
    public String selectResource(){
        return "select资源";
    }

}

