package com.xiaoliu.straw.portal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoliu.straw.portal.model.Answer;
import com.xiaoliu.straw.portal.vo.AnswerVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaoliu
 * @since 2021-05-20
 */
public interface IAnswerService extends IService<Answer> {

    // 添加回答
    Answer saveAnswer(AnswerVo answerVo,String username);

    // 根据问题id查询所有回答
    List<Answer> getAnswerByQuestionID(Integer questinoId);

    //采纳回答方法
    boolean accept(Integer answerId);
}
