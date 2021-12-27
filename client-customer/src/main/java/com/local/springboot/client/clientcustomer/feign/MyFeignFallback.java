package com.local.springboot.client.clientcustomer.feign;

import org.springframework.stereotype.Component;

/**
 * feign调用容错处理
 */
@Component
public class MyFeignFallback implements MyFeign {

    @Override
    public String getUser(String id) {
        return null;
    }

    @Override
    public String exception() {
        return "网络请求超时，请稍后重试！";
    }

    @Override
    public String ribbonTest() {
        return null;
    }
}
