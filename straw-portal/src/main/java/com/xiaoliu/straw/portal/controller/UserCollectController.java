package com.xiaoliu.straw.portal.controller;


import com.xiaoliu.straw.portal.service.IUserCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiaoliu
 * @since 2021-05-20
 */
@RestController
@RequestMapping("/v1/userCollects")
public class UserCollectController {

    @Autowired
    private IUserCollectService userCollectService;


    @GetMapping("/countCollections")
    public Integer countCollections(Integer userId){
        return userCollectService.countCollectionsByUserId(userId);
    }

}
