package com.xiaoliu.straw.portal;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.xiaoliu.straw.portal.mapper.ClassroomMapper;
import com.xiaoliu.straw.portal.mapper.UserMapper;
import com.xiaoliu.straw.portal.model.Classroom;
import com.xiaoliu.straw.portal.model.Question;
import com.xiaoliu.straw.portal.model.User;
import com.xiaoliu.straw.portal.service.IQuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StrawPortalApplicationTests {

    @Autowired
    UserMapper userMapper;
    @Test
    void contextLoads() {
        User user=userMapper.selectById(3);
        System.out.println(user);
    }

    @Autowired
    ClassroomMapper classroomMapper;
    @Test
    void wapperTest(){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("name","JSD2003");
        Classroom classroom=classroomMapper.selectOne(queryWrapper);
        System.out.println(classroom);
    }

    @Autowired
    IQuestionService questionService;
    @Test
    void testQuestion(){
        String username="xiaom";
        PageInfo<Question> list= questionService.getMyQuestions(username,1,8);
        //list.forEach(q-> System.out.println(q));

    }
}
