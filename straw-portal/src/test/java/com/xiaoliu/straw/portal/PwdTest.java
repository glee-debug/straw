package com.xiaoliu.straw.portal;

import com.xiaoliu.straw.portal.mapper.AnswerMapper;
import com.xiaoliu.straw.portal.mapper.QuestionMapper;
import com.xiaoliu.straw.portal.mapper.UserMapper;
import com.xiaoliu.straw.portal.model.Answer;
import com.xiaoliu.straw.portal.model.Question;
import com.xiaoliu.straw.portal.model.Role;
import com.xiaoliu.straw.portal.service.IQuestionService;
import com.xiaoliu.straw.portal.service.IUserService;
import com.xiaoliu.straw.portal.vo.QuestionVo;
import com.xiaoliu.straw.portal.vo.UserVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@SpringBootTest
public class PwdTest {
    @Test
    void encode(){
        // 实例化spring-security提供的加密对象
        // 下面的对象是按照BCrypt加密规则执行加密的对象
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        // 将指定字符串加密为密文的方法如下
        // 随机加盐技术 不可逆
        String pwd=encoder.encode("888888");
        System.out.println(pwd);
        Boolean flg=encoder.matches("888887",pwd);
        System.out.println(flg);
        flg=encoder.matches("888888",pwd);
        System.out.println(flg);
    }

    @Autowired
    IQuestionService questionService;
    @Test
    void add(){
        QuestionVo questionVo = new QuestionVo()
                .setTitle("questionVoTest")
                .setContent("好使不")
                .setTagNames(new String[]{"Web","Ajax"})
                .setTeacherNicknames(new String[]{"王克晶"});
        questionService.saveQuestion(questionVo,"st2");
    }

    @Autowired
    IUserService userService;
    @Test
    void getUservo(){
        UserVo userVo= userService.getCurrentUserVo("wangkj");
        System.out.println(userVo);
    }

    @Autowired
    QuestionMapper questionMapper;
    @Test
    void testTeacherQuestion(){
        List<Question> list=questionMapper.findTeacherQuestions(4);
        list.forEach(l-> System.out.println(l));
    }
    @Autowired
    UserMapper userMapper;

    @Test
    void testRole(){
        List<Role> list=userMapper.findUserRoles(4);
        list.forEach(l-> System.out.println(l));
    }

    @Autowired
    AnswerMapper answerMapper;
    @Test
    void answers(){
        List<Answer> list=answerMapper.findAnswerWithCommentByQuestionId(15);
        list.forEach(l-> System.out.println(l));
    }
}
