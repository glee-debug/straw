package com.xiaoliu.straw.portal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoliu.straw.portal.model.User;
import com.xiaoliu.straw.portal.vo.RegisterVo;
import com.xiaoliu.straw.portal.vo.UserVo;
import org.springframework.security.core.userdetails.UserDetails;

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

    UserDetails getUserDetails(String username);

    // 注册学生的方法
    // 为使用异常表示运行的状态返回值直接使用void
    // 参数方面,控制器接收到了RegisterVo的对象
    void registerStudent(RegisterVo registerVo);

    List<User> getMaster();

    Map<String,User> getTeacherMap();

    // 获得当前登录用户的信息
    UserVo getCurrentUserVo(String username);


}
