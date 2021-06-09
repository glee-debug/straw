package cn.tedu.straw.faq.ribbon;

import cn.tedu.straw.commons.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RibbonClient {

    @Autowired
    RestTemplate restTemplate;

    public User getUser(String username){
        String url = "http://sys-service/v1/auth/user?username={1}";
        User user = restTemplate.getForObject(url,User.class,username);
        return user;
    }
}
