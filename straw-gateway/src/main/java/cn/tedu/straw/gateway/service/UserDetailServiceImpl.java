package cn.tedu.straw.gateway.service;

import cn.tedu.straw.commons.model.Permission;
import cn.tedu.straw.commons.model.Role;
import cn.tedu.straw.commons.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名获得用户对象的rest接口
        String url = "http://sys-service/v1/auth/user?username={1}";
        // 参数是url{}中占位符用的
        User user = restTemplate.getForObject(url, User.class,username);
        log.debug("查询出的用户:{}",user);
        // 判断用户不为空
        if (user==null){
            throw new UsernameNotFoundException("用户名或密码错误!");
        }
        // 调用根据用户id获得用户权限的rest接口
        url = "http://sys-service/v1/auth/permissions?userId={1}";
        // 凡是Rest接口返回List格式的,调用时使用对应泛型的数组类型接收
        // 原因是传递过程中数据是json格式,json格式是js代码,js代码没有List类型
        Permission[] permissions = restTemplate.getForObject(url,Permission[].class,user.getId());
        // 调用根据用户id获得用户角色的rest接口
        url = "http://sys-service/v1/auth/roles?userId={1}";
        Role[] roles = restTemplate.getForObject(url, Role[].class,user.getId());
        if (permissions==null || roles==null){
            throw new UsernameNotFoundException("当前用户权限不足");
        }
        // 将权限和角色赋值到auth数组中
        int i = 0 ;
        String[] auth = new String[permissions.length+roles.length];
        for (Permission p:permissions){
            auth[i++] = p.getName();
        }
        for (Role r:roles){
            auth[i++] = r.getName();
        }
        // 构造userDetails对象
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(auth)
                .accountLocked(user.getLocked()==1)
                .disabled(user.getEnabled()==0)
                .build();
        return userDetails;
    }
}
