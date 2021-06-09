package com.xiaoliu.straw.portal.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoliu.straw.portal.mapper.UserCollectMapper;
import com.xiaoliu.straw.portal.model.UserCollect;
import com.xiaoliu.straw.portal.service.IUserCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiaoliu
 * @since 2021-05-20
 */
@Service
public class UserCollectServiceImpl extends ServiceImpl<UserCollectMapper, UserCollect> implements IUserCollectService {

    @Autowired
    UserCollectMapper userCollectMapper;

    @Override
    public Integer countCollectionsByUserId(Integer userId) {
        QueryWrapper<UserCollect> query = new QueryWrapper<>();
        query.eq("user_id",userId);
        // 查询收藏数
        Integer count = userCollectMapper.selectCount(query);
        return count;
    }
}
