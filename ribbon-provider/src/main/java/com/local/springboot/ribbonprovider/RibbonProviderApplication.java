package com.local.springboot.ribbonprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class RibbonProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(RibbonProviderApplication.class, args);
    }

}
