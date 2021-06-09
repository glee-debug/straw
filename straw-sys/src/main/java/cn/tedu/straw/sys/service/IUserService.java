package cn.tedu.straw.sys.service;

import cn.tedu.straw.commons.model.Permission;
import cn.tedu.straw.commons.model.Role;
import cn.tedu.straw.commons.model.User;
import cn.tedu.straw.sys.vo.RegisterVo;
import cn.tedu.straw.sys.vo.UserVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaoliu
 * @since 2021-05-20
 */
public interface IUserService extends IService<User> {
/*
    UserDetails getUserDetails(String username);*/

    // 注册学生的方法
    // 为使用异常表示运行的状态返回值直接使用void
    // 参数方面,控制器接收到了RegisterVo的对象
    void registerStudent(RegisterVo registerVo);

    List<User> getMaster();

    Map<String,User> getTeacherMap();

    // 获得当前登录用户的信息
    UserVo getCurrentUserVo(String username);

    // 微服务rest接口中需要使用的方法
    User getUserByUsername(String username);

    List<Permission> getUserPermissions(Integer userId);

    List<Role> getUserRoles(Integer userId);
}
