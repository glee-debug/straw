package cn.tedu.straw.faq;

import cn.tedu.straw.commons.model.Question;
import cn.tedu.straw.faq.service.IQuestionService;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StrawFaqApplicationTests {

    @Autowired
    IQuestionService questionService;

    @Test
    void contextLoads() {
        PageInfo<Question> questions = questionService.getMyQuestions("st2",1,8);
        questions.getList().forEach(question -> System.out.println(question));
        System.out.println("================");
        System.out.println(questions);
    }

}
