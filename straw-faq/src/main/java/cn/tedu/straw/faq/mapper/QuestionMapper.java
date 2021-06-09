package cn.tedu.straw.faq.mapper;

import cn.tedu.straw.commons.model.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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
    public interface QuestionMapper extends BaseMapper<Question> {
        @Select("SELECT DISTINCT q.* FROM question q LEFT JOIN user_question uq ON q.id = uq.question_id " +
                "WHERE q.user_id = #{userId} OR uq.user_id=#{userId} ORDER BY q.createtime desc")
    List<Question> findTeacherQuestions(Integer userId);

        @Update("update question set status=#{status} where id=#{questionId}")
    int updateStatus(Integer questionId,Integer status);
    }
