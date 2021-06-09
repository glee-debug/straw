package cn.tedu.straw.sys.controller;

import cn.tedu.straw.commons.model.Permission;
import cn.tedu.straw.commons.model.User;
import cn.tedu.straw.commons.service.ServiceException;
import cn.tedu.straw.commons.vo.R;
import cn.tedu.straw.sys.mapper.UserMapper;
import cn.tedu.straw.sys.service.IUserService;
import cn.tedu.straw.sys.vo.RegisterVo;
import cn.tedu.straw.sys.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
@Slf4j
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

    // 查询所有老师的rest接口
    @GetMapping("/teacher")
    public List<User> teacher(){
        List<User> teachers = userService.getMaster();
        return teachers;
    }

    // 查询所有老师的控制层方法
    @GetMapping("/masters")
    public R<List<User>> masters(){
        List<User> teachers = userService.getMaster();
        return R.ok(teachers);
    }

    @GetMapping("/me")
    public R<UserVo> me(@AuthenticationPrincipal UserDetails userDetails){
        UserVo userVo = userService.getCurrentUserVo(userDetails.getUsername());
        return R.ok(userVo);
    }

    @PostMapping("/register")
    // 当一个实体类前加了@Validated注解
    // 表示这个实体类的内容要被SpringValidation验证
    // 验证完毕之后会生成一个BindingResult类型对象
    // 这个对象中保存着验证的结果信息
    public R registerStudent(@Validated RegisterVo registerVo,
                             BindingResult bindingResult){
        //log.debug("注册信息:{}",registerVo);

        if (bindingResult.hasErrors()){
            // 获得这个错误
            String error=bindingResult.getFieldError().getDefaultMessage();
            return R.unproecsableEntity(error);
        }
        if (!registerVo.getPassword().equals(registerVo.getConfirm())) {
            return R.unproecsableEntity("两次输入密码不一致");
        }
        try{
            userService.registerStudent(registerVo);
            return R.ok("注册完成");
        }catch (ServiceException e){
            log.error("注册失败",e);
            return R.failed(e);
        }
    }

}
