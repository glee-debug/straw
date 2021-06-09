package com.xiaoliu.straw.portal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoliu.straw.portal.model.Permission;
import com.xiaoliu.straw.portal.model.Role;
import com.xiaoliu.straw.portal.model.User;
import com.xiaoliu.straw.portal.vo.UserVo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* <p>
    *  Mapper 接口
    * </p>
*
* @author xiaoliu
* @since 2021-05-20
*/
    @Repository
    public interface UserMapper extends BaseMapper<User> {
    //根据用户名查询用户对象
    @Select("select * from user where username=#{username}")
    User findUserByUsername(String username);

    // 根据用户id查询权限
    @Select("SELECT p.id,p.name FROM user u LEFT JOIN user_role ur ON u.id=ur.user_id " +
            "LEFT JOIN role r ON ur.role_id = r.id " +
            "LEFT JOIN role_permission rp ON r.id =rp.role_id " +
            "LEFT JOIN permission p ON rp.permission_id=p.id " +
            "WHERE u.id=#{id}")
    List<Permission> findUserPermissionById(Integer id);

    // 根据当前登录的用户名查询uservoxinxi
    @Select("select id,username,nickname from user where username=#{username}")
    UserVo findUserVoByUsername(String username);

    @Select("SELECT r.id,r.name FROM user " +
            "LEFT JOIN user_role ur ON user.id = ur.user_id " +
            "LEFT JOIN role r ON ur.role_id=r.id " +
            "WHERE user.id=#{userId}")
    List<Role> findUserRoles(Integer userId);

    }
