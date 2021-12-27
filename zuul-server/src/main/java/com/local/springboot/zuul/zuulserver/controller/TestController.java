package com.local.springboot.zuul.zuulserver.controller;

import com.local.springboot.zuul.zuulserver.feign.SsoFeign;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/set")
public class TestController {

    @Resource
    private SsoFeign feign;

    @RequestMapping("/test")
    public String test() {
        return "hello world!";
    }

    @RequestMapping("/feign/{accessToken}")
    public boolean feign(@PathVariable("accessToken") String accessToken) {
        return feign.checkAccessToken(accessToken);
    }

    @GetMapping("/info/{id}")
    public String get(@PathVariable("id") String id) {
        return feign.get(id);
    }

}
