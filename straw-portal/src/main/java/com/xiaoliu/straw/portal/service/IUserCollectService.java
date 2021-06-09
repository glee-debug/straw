package com.xiaoliu.straw.portal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoliu.straw.portal.model.UserCollect;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaoliu
 * @since 2021-05-20
 */
public interface IUserCollectService extends IService<UserCollect> {
    // 根据用户id查询该用户收藏数的方法
    Integer countCollectionsByUserId(Integer userId);
}
