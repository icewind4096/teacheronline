package com.windvalley.guli.service.ucenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.windvalley.guli"})
//允许注册到注册中心
@EnableDiscoveryClient
public class ServiceUCenterApplication {
    public static void main(String[] args){
        SpringApplication.run(ServiceUCenterApplication.class, args);
    }
}
