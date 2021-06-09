package com.xiaoliu.straw.portal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Slf4j
public class HomeController {
    // spring-security角色类型固定
    public static final GrantedAuthority STUDENT = new SimpleGrantedAuthority("ROLE_STUDENT");
    public static final GrantedAuthority TEACHER = new SimpleGrantedAuthority("ROLE_TEACHER");
    // 用于显示学生首页的控制器方法
    @GetMapping("/index.html")
    public ModelAndView index(@AuthenticationPrincipal UserDetails user){
        if (user.getAuthorities().contains(TEACHER)){
            return new ModelAndView("index_teacher");
        }else if(user.getAuthorities().contains(STUDENT)){
            return new ModelAndView("index");
        }
        // 既不是学生也不是老师
        return new ModelAndView("login");
    }

    // 显示发布问题的页面的控制器方法
    @GetMapping("/question/create.html")
    public ModelAndView createQuestion(){
        return new ModelAndView("question/create");
    }

    @GetMapping("/question/detail.html")
    public ModelAndView detailView(@AuthenticationPrincipal UserDetails user){
        if (user.getAuthorities().contains(TEACHER)){
            return new ModelAndView("question/detail_teacher");
        }else if(user.getAuthorities().contains(STUDENT)){
            return new ModelAndView("question/detail");
        }
        // 既不是学生也不是老师
        return new ModelAndView("login");
    }
}
