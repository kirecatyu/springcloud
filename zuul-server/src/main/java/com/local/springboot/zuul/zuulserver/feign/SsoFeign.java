package com.local.springboot.zuul.zuulserver.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "zuul-server", path = "/sso")
public interface SsoFeign {

    @RequestMapping("/checkAccessToken/{accessToken}")
    boolean checkAccessToken(@PathVariable("accessToken") String accessToken);

    @GetMapping("/info/{id}")
    String get(@PathVariable("id") String id);
}
