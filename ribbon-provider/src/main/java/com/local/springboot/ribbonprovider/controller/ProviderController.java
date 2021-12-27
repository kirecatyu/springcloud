package com.local.springboot.ribbonprovider.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class ProviderController {

    @Value("${server.port}")
    private String port;

    @RequestMapping("/provider")
    public String provider() {
        return "我是服务提供者";
    }

    @GetMapping("/user/{id}")
    public String user(@PathVariable(value = "id") String id) {
        return "我是服务提供者==>用户id:" + id;
    }

    @GetMapping("/exception")
    public String exception() {
        throw new RuntimeException("服务器异常");
    }

    @GetMapping("/ribbonTest")
    public String ribbonTest() {
        return "我是服务提供者 -- 我的端口是：" + port;
    }
}
