package com.xiaoliu.straw.portal.controller;

import com.xiaoliu.straw.portal.mapper.UserMapper;
import com.xiaoliu.straw.portal.model.Permission;
import com.xiaoliu.straw.portal.model.User;
import com.xiaoliu.straw.portal.service.IUserService;
import com.xiaoliu.straw.portal.vo.R;
import com.xiaoliu.straw.portal.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiaoliu
 * @since 2021-05-20
 */
@RestController
@RequestMapping("/v1/users")
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    UserMapper userMapper;

    @GetMapping("/get")
    //preauthorize规定当前控制器需要特殊权限才能访问
    @PreAuthorize("hasAuthority('/user/get')")
    public User get(Integer id){
        User user=userService.getById(id);
        // 控制器方法返回一个java对象,这个对象会自动转换为json格式
        return user;
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('/user/list')")
    public List<Permission> list(){
        List<Permission> list=userMapper.findUserPermissionById(3);
        return list;
    }

    @GetMapping("/teacher")
    public R<List<User>> teacher(){
        List<User> teachers = userService.getMaster();
        return R.ok(teachers);
    }

    @GetMapping("/me")
    public R<UserVo> me(@AuthenticationPrincipal UserDetails userDetails){
        UserVo userVo = userService.getCurrentUserVo(userDetails.getUsername());
        return R.ok(userVo);
    }

}
