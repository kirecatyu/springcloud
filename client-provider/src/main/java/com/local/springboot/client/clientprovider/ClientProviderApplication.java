package com.local.springboot.client.clientprovider;

//import com.local.springboot.client.clientprovider.config.RibbonConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
//import org.springframework.cloud.netflix.ribbon.RibbonClient;

import java.math.BigDecimal;

//@RibbonClient(name = "client-provider-server", configuration = RibbonConfig.class)
@EnableDiscoveryClient
@SpringBootApplication
public class ClientProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientProviderApplication.class, args);
    }

}
