package com.local.springboot.client.clientcustomer.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * FallbackFactory工厂，获取HTTP请求错误状态码和信息
 * （只打印异常）
 */
@Component
@Slf4j
public class MyFeignFallbackFactory implements FallbackFactory<MyFeign> {

    private final MyFeignFallback myFeignFallback;

    public MyFeignFallbackFactory(MyFeignFallback myFeignFallback) {
        this.myFeignFallback = myFeignFallback;
    }

    @Override
    public MyFeign create(Throwable cause) {
        // 打印异常
        log.info("出现异常：{}", cause);
        cause.printStackTrace();
        return myFeignFallback;
    }
}
