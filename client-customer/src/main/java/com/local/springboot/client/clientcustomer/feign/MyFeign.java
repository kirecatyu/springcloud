package com.local.springboot.client.clientcustomer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "client-provider-server", path = "/api", fallback = MyFeignFallback.class
        , fallbackFactory = MyFeignFallbackFactory.class)
public interface MyFeign {

    @GetMapping("/user/{id}")
    String getUser(@PathVariable("id") String id);

    @GetMapping("/exception")
    String exception();

    @GetMapping("/ribbonTest")
    String ribbonTest();
}
