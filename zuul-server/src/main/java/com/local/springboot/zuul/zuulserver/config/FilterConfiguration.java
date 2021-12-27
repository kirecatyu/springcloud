package com.local.springboot.zuul.zuulserver.config;

import com.local.springboot.zuul.zuulserver.filter.MyAccessFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {

    @Bean
    public MyAccessFilter myAccessFilter(){
        return new MyAccessFilter();
    }
}
