package cn.tedu.straw.sys.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/demo")
public class DemoController {
    @GetMapping("/hello")
    public String demo(){
        return "hello sys!!!";
    }

    // 测试共享session效果的控制器方法
    @GetMapping("/wo")
    public String wo(@AuthenticationPrincipal UserDetails userDetails){
        System.out.println("当前登录用户名:"+userDetails.getUsername());
        return "用户名:"+userDetails;
    }

}
