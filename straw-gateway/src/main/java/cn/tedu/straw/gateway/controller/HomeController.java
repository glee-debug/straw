package cn.tedu.straw.gateway.controller;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// 添加thymeleaf依赖controller控制器中的方法返回字符串就可以指定模板名称
@Controller
public class HomeController {

    public static final GrantedAuthority TEACHER = new SimpleGrantedAuthority("ROLE_TEACHER");
    public static final GrantedAuthority STUDENT = new SimpleGrantedAuthority("ROLE_STUDENT");

    @GetMapping("/register.html")
    public String register(){
        return "register";
    }

    @GetMapping("/index.html")
    public String index(@AuthenticationPrincipal UserDetails userDetails){
        if (userDetails.getAuthorities().contains(TEACHER)){
            return "index_teacher";
        }else if (userDetails.getAuthorities().contains(STUDENT)){
            return "index";
        }else {
            return null;
        }
    }

    @GetMapping("/question/create.html")
    public String create(){
        return "question/create";
    }

}
