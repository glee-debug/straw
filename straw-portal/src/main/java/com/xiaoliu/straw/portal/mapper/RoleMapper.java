package com.xiaoliu.straw.portal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoliu.straw.portal.model.Role;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
* <p>
    *  Mapper 接口
    * </p>
*
* @author xiaoliu
* @since 2021-05-20
*/
    @Repository
    public interface RoleMapper extends BaseMapper<Role> {
    }
