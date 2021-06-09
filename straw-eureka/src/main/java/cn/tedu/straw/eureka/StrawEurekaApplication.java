package cn.tedu.straw.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
// 启用eureka服务器的功能
// 这样这个项目在启动时就会按照eureka服务器的配置进行启动
@EnableEurekaServer
public class StrawEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(StrawEurekaApplication.class, args);
    }

}
