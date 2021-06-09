package com.xiaoliu.straw.portal.controller;


import com.xiaoliu.straw.portal.model.Tag;
import com.xiaoliu.straw.portal.service.ITagService;
import com.xiaoliu.straw.portal.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/v1/tags")
public class TagController {

    @Autowired
    ITagService tagService;

    @GetMapping("")
    public R<List<Tag>> tags(){
        List<Tag> list = tagService.getTags();
        return R.ok(list);
        // 利用R对象返回查询到的所有标签
    }
}
