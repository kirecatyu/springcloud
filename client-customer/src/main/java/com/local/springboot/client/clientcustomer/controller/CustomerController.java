package com.local.springboot.client.clientcustomer.controller;

import com.local.springboot.client.clientcustomer.feign.MyFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

@RestController
@SuppressWarnings("all")
public class CustomerController {
    @Autowired
    private DiscoveryClient discoveryClient;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private MyFeign myFeign;

    @RequestMapping("/customer")
    public String customer() {
        return "我是服务消费者";
    }
    /**
     * 访问服务
     */
    private static final String REST_URL_PREFIX = "http://CLIENT-PROVIDER-SERVER";

    @RequestMapping("/query")
    public String query() {
        List<ServiceInstance> instances = discoveryClient.getInstances("client-provider-server");
        StringBuilder urls = new StringBuilder();
//        for (ServiceInstance instance : instances) {
//            urls.append(instance.getHost() + ":" + instance.getPort()).append(",");
//        }
        // 没有集群，只有一个
        ServiceInstance instance = instances.get(0);
        // 使用restTemplate发起请求
        String result = restTemplate.getForEntity("http://CLIENT-PROVIDER-SERVER/provider", String.class).getBody();
        return result;
    }

    @RequestMapping("/queryService")
    public String queryService() {
        List<ServiceInstance> instances = discoveryClient.getInstances("client-provider-server");
        StringBuilder urls = new StringBuilder();
        for (ServiceInstance instance : instances) {
            urls.append(instance.getHost() + ":" + instance.getPort()).append(",");
        }
        return urls.toString();
    }

    /**
     * feign远程调用
     */
    @GetMapping("/query/{id}")
    public String getUser(@PathVariable(value = "id") String id) {
        return myFeign.getUser(id);
    }

    /**
     * feign远程调用
     */
    @GetMapping("/exception")
    public String exception() {
        return myFeign.exception();
    }

    /**
     * feign负载均衡测试
     *
     * @return
     */
    @RequestMapping("/feign/testRibbon")
    public String testRibbon1() {
        return myFeign.ribbonTest();
    }

    /**
     * restTemplate负载均衡测试
     *
     * @return
     */
    @RequestMapping("/restTemplate/testRibbon")
    public String testRibbon2() {
        // 使用restTemplate发起请求
        String result = restTemplate.getForEntity(
                REST_URL_PREFIX + "/api/ribbonTest", String.class).getBody();
        return result;
    }
}
