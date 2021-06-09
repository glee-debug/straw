package cn.tedu.straw.faq.mapper;


import cn.tedu.straw.commons.model.Answer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
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
    public interface AnswerMapper extends BaseMapper<Answer> {
        // 根据问题id查询所有回答 回答中包含这个回答包含的所有评论
    List<Answer> findAnswerWithCommentByQuestionId(Integer questionId);

    // 修改当前回答的采纳状态的方法
    @Update("update answer set accept_status = 1 where id=#{answerId}")
    int updateStauts(@Param("answerId") Integer answerId,
                     @Param("acceptStatus") Integer acceptStatus);

    }
