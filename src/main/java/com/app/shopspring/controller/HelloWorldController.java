package com.app.shopspring.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @RequestMapping("/hello")
    public String hello(){
        return "hello world";
    }

    @PostMapping("/hello")
    public String helloPost(@RequestBody String name){
        return "hello post "+name;
    }

}
