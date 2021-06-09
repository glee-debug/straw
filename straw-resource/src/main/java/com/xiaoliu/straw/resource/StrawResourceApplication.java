package com.xiaoliu.straw.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
// 当前项目会以eureka客户端启动
// 并在配置文件中指定位置寻找注册中心进行注册
@EnableEurekaClient
public class StrawResourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StrawResourceApplication.class, args);
    }

}
