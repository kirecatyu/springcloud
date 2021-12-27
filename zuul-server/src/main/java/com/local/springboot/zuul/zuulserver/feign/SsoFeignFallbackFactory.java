package com.local.springboot.zuul.zuulserver.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * FallbackFactory工厂，获取HTTP请求错误状态码和信息
 * （只打印异常）
 */
@Component
@Slf4j
public class SsoFeignFallbackFactory implements FallbackFactory<SsoFeign> {

    private final SsoFeignFallback myFeignFallback;

    public SsoFeignFallbackFactory(SsoFeignFallback myFeignFallback) {
        this.myFeignFallback = myFeignFallback;
    }

    @Override
    public SsoFeign create(Throwable cause) {
        // 打印异常
        log.info("出现异常：{}", cause);
        cause.printStackTrace();
        return myFeignFallback;
    }
}
