package com.xiaoliu.straw.portal.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

// 当前类是一个spring配置类
@Configuration
// prePostEnabled = true启动权限管理功能 默认false
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailServiceImpl userDetailService;

    // AuthencationManagerBuilder 用户登录验证信息的
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /*// 在内存中设置一个用户
        auth.inMemoryAuthentication().withUser("st1")
                .password("{bcrypt}$2a$10$xdNdHni4aq/.NfqVfnF1X.McrNyLELkJo0yT.6BOjRlOUBNcV81jm")
                // 设置用户特殊权限
                .authorities("/user/get",
                        "/user/list");
        // 配置了用户后 application中的设置就失效了*/
        auth.userDetailsService(userDetailService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()//关闭防跨域攻击的功能,不关闭容易出错
        .authorizeRequests()// 设置请求的授权
         // 配置下列路径放行 不需要登录就能访问
        .antMatchers(
                "/js/*",
                "/css/*",
                "/img/**",//放行img下全部目录
                "/bower_components/**",
                "/login.html",
                "/register.html",
                "/register"
        )// 其他所有请求需要登录验证
        .permitAll().anyRequest().authenticated()
                .and()//上面配置完毕开始另一个配置
        .formLogin()//使用表单登录
        .loginPage("/login.html")//设置登录页面路径
        .loginProcessingUrl("/login")// 设置处理登录的路径
        .failureUrl("/login.html?error")//登录失败访问页面
        .defaultSuccessUrl("/index.html")//登录成功访问的页面
        .and()
        .logout()//设置登出信息
        .logoutUrl("/logout")//登出路径
        .logoutSuccessUrl("/login.html?logout"); // 登出后现实的页面

    }
}
