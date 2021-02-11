package com.windvalley.guli.service.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan({"com.windvalley.guli"})
@EnableDiscoveryClient
public class ServiceOSSApplication {
    public static void main(String[] args){
        SpringApplication.run(ServiceOSSApplication.class, args);
    }
}
