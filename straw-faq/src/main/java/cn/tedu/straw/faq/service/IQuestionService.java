package cn.tedu.straw.faq.service;

import cn.tedu.straw.commons.model.Question;
import cn.tedu.straw.faq.vo.QuestionVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaoliu
 * @since 2021-05-20
 */
public interface IQuestionService extends IService<Question> {

    PageInfo<Question> getMyQuestions(String username, Integer pageNum, Integer pageSize);

    // 新增学生发布问题的方法
    void saveQuestion(QuestionVo questionVo, String username);

    // 根据用户id查询该用户问题数的方法
    Integer countQuestionsByUserId(Integer userId);

    // 分页查询讲师问题列表的方法
    PageInfo<Question> getQuestionsByTeacherName(String username,Integer pageNum,Integer pageSize);

    Question getQuestionById(Integer id);
}
