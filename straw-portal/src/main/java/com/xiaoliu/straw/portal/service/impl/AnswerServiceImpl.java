package com.xiaoliu.straw.portal.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoliu.straw.portal.mapper.AnswerMapper;
import com.xiaoliu.straw.portal.mapper.QuestionMapper;
import com.xiaoliu.straw.portal.mapper.UserMapper;
import com.xiaoliu.straw.portal.model.Answer;
import com.xiaoliu.straw.portal.model.Question;
import com.xiaoliu.straw.portal.model.User;
import com.xiaoliu.straw.portal.service.IAnswerService;
import com.xiaoliu.straw.portal.service.ServiceException;
import com.xiaoliu.straw.portal.vo.AnswerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiaoliu
 * @since 2021-05-20
 */
@Service
public class AnswerServiceImpl extends ServiceImpl<AnswerMapper, Answer> implements IAnswerService {

    @Autowired
    AnswerMapper answerMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    @Transactional // 声明式事务
    public Answer saveAnswer(AnswerVo answerVo, String username) {
        User user = userMapper.findUserByUsername(username);
        Answer answer = new Answer()
                .setContent(answerVo.getContent())
                .setLikeCount(0)
                .setUserId(user.getId())
                .setUserNickName(user.getNickname())
                .setQuestId(answerVo.getQuestionId())
                .setCreatetime(LocalDateTime.now())
                .setAcceptStatus(0);
        int num = answerMapper.insert(answer);
        if (num!=1){
            throw ServiceException.busy();
        }
        return answer;
    }

    @Override
    public List<Answer> getAnswerByQuestionID(Integer questinoId) {
        /*QueryWrapper<Answer> query = new QueryWrapper<>();
        query.eq("quest_id",questinoId);
        List<Answer> answers = answerMapper.selectList(query);*/
        // 查询包含评论的回答
        List<Answer> answers = answerMapper.findAnswerWithCommentByQuestionId(questinoId);
        return answers;
    }

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    @Transactional
    public boolean accept(Integer answerId) {
        Answer answer = answerMapper.selectById(answerId);
        if (answer.getAcceptStatus()==1){
            throw ServiceException.notFound("该回答已经被采纳");
        }
        int num = answerMapper.updateStauts(answerId,1);
        if (num!=1){
            throw ServiceException.busy();
        }
        num = questionMapper.updateStatus(answer.getQuestId(), Question.SOLVED);
        if (num!=1){
            throw ServiceException.busy();
        }
        return true;
    }
}
