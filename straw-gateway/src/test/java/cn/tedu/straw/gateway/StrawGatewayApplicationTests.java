package cn.tedu.straw.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@SpringBootTest
class StrawGatewayApplicationTests {

    @Autowired
    RestTemplate restTemplate;

    @Test
    void contextLoads() {
        // 先定义要调用的rest接口的url
        // 微服务各模块件调用不需要路由
        String url=new String("http://sys-service/v1/auth/demo");
        // 发送调用请求
        // 参数1:请求路径 ; 参数2:返回值类型的反射
        String str= restTemplate.getForObject(url,String.class);
        System.out.println("返回值为:"+str);

    }

    /*
    * 这个redisTemplate对象是springboot提供的
    * 使用之前就已经在spring容器中*/
    @Resource
    RedisTemplate<String,String> redisTemplate;

    @Test
    void redis(){
        // 新增信息到redis
        //redisTemplate.opsForValue().set("msg","hello World");
        // 获得redis中的信息
        String str = redisTemplate.opsForValue().get("msg");
        System.out.println("运行完毕:"+str);
        // 删除一个保存的信息
        redisTemplate.delete("msg");
        System.out.println("运行完毕:"+str);
    }

}
