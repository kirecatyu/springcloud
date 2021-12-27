package com.local.springboot.zuul.zuulserver.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * feign调用容错处理
 */
@Component
@Slf4j
public class SsoFeignFallback implements SsoFeign {


    @Override
    public boolean checkAccessToken(String accessToken) {
        log.info("Redis 异常");
        return false;
    }

    @Override
    public String get(String id) {
        return null;
    }
}
