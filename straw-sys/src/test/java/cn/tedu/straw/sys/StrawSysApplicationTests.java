package cn.tedu.straw.sys;

import cn.tedu.straw.commons.model.User;
import cn.tedu.straw.sys.mapper.UserMapper;
import cn.tedu.straw.sys.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class StrawSysApplicationTests {

    @Autowired
    UserMapper userMapper;
    @Test
    void contextLoads() {
        User user = userMapper.findUserByUsername("tc2");
        System.out.println(user);
    }

    @Autowired
    IUserService userService;
    @Test
    void  getMaster(){
        List<User> user = userService.getMaster();
        user.forEach(u-> System.out.println(u));
    }

}
