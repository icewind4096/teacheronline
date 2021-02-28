package com.windvalley.guli.service.vod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan({"com.windvalley.guli"})
//允许注册到注册中心
@EnableDiscoveryClient
public class ServiceVODApplication {
    public static void main(String[] args){
        SpringApplication.run(ServiceVODApplication.class, args);
    }
}
