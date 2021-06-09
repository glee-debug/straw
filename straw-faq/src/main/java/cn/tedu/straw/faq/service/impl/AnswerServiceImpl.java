package cn.tedu.straw.faq.service.impl;


import cn.tedu.straw.commons.model.Answer;
import cn.tedu.straw.commons.model.Question;
import cn.tedu.straw.commons.model.User;
import cn.tedu.straw.commons.service.ServiceException;
import cn.tedu.straw.faq.mapper.AnswerMapper;
import cn.tedu.straw.faq.mapper.QuestionMapper;
import cn.tedu.straw.faq.ribbon.RibbonClient;
import cn.tedu.straw.faq.service.IAnswerService;
import cn.tedu.straw.faq.vo.AnswerVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
    RibbonClient ribbonClient = new RibbonClient();

    @Override
    @Transactional // 声明式事务
    public Answer saveAnswer(AnswerVo answerVo, String username) {
        User user = ribbonClient.getUser(username);
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
