package cn.tedu.straw.faq.service.impl;


import cn.tedu.straw.commons.model.*;
import cn.tedu.straw.commons.service.ServiceException;
import cn.tedu.straw.faq.mapper.QuestionMapper;
import cn.tedu.straw.faq.mapper.QuestionTagMapper;
import cn.tedu.straw.faq.mapper.UserQuestionMapper;
import cn.tedu.straw.faq.service.IQuestionService;
import cn.tedu.straw.faq.service.ITagService;
import cn.tedu.straw.faq.vo.QuestionVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiaoliu
 * @since 2021-05-20
 */
@Service
@Slf4j
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Override
    public PageInfo<Question> getMyQuestions(String username, Integer pageNum, Integer pageSize) {
        User user = getUser(username);
        QueryWrapper<Question> query = new QueryWrapper<>();
        query.eq("user_id",user.getId());
        query.eq("delete_status",0);
        query.orderByDesc("createtime");
        // pagehelper提供的方法 startpage规定查询第几页 每页多少条
        PageHelper.startPage(pageNum,pageSize);
        List<Question> list=questionMapper.selectList(query);
        log.debug("找到问题数:{}",list.size());
        // 遍历所有查询出来的问题,将每个问题的标签的list都附上值
        for(Question q:list){
            List<Tag> tags = tagNames2Tags(q.getTagNames());
            q.setTags(tags);
        }
        return new PageInfo<Question>(list);
    }

    @Autowired
    QuestionTagMapper questionTagMapper;
    @Autowired
    UserQuestionMapper userQuestionMapper;
    @Autowired
    RestTemplate restTemplate;

    @Override
    // 凡是增删改操作的业务逻辑层方法都是要添加声明式事务注解transactional
    @Transactional
    public void saveQuestion(QuestionVo questionVo, String username) {
        User user=getUser(username);
        log.debug("获得当前用户信息:{}",user);
        StringBuffer sb = new StringBuffer();
        for (String s:questionVo.getTagNames()){
            sb.append(s);
            sb.append(",");
        }
        String tagNames = sb.deleteCharAt(sb.lastIndexOf(",")).toString();
        Question question = new Question()
                .setTitle(questionVo.getTitle())
                .setContent(questionVo.getContent())
                .setUserNickName(user.getNickname())
                .setUserId(user.getId())
                .setCreatetime(LocalDateTime.now())
                .setStatus(0)
                .setPublicStatus(0)
                .setPageViews(0)
                .setDeleteStatus(0)
                .setTagNames(tagNames);

        int num = questionMapper.insert(question);
        if (num!=1){
            throw ServiceException.busy();
        }
        log.debug("新增了问题对象:{}",question);
        // 新增问题和标签的关系
        Map<String,Tag> name2TagMap = tagService.getNameToTagMap();
        for (String tag:questionVo.getTagNames()){
            QuestionTag qt = new QuestionTag()
                    .setQuestionId(question.getId())
                    .setTagId(name2TagMap.get(tag).getId());
            questionTagMapper.insert(qt);
            if (num!=1){
                throw ServiceException.busy();
            }
            log.debug("新增了问题标签对象:{}",qt);
        }
        // 新增问题和讲师的关系
        String url = "http://sys-service/v1/users/teacher";
        User[] users = restTemplate.getForObject(url,User[].class);
        Map<String,User> masters = new HashMap<>();
        for (User u:users){
            masters.put(u.getNickname(),u);
        }
        for (String teacher:questionVo.getTeacherNicknames()){
            UserQuestion uq = new UserQuestion()
                    .setUserId(user.getId())
                    .setQuestionId(question.getId())
                    .setCreatetime(LocalDateTime.now());
            num = userQuestionMapper.insert(uq);
            if (num!=1){
                throw ServiceException.busy();
            }
            log.debug("新增了问题老师对象:{}",uq);
        }
    }

    @Autowired
    ITagService tagService;

    // 根据tag_names列的值获得它对应的标签集合的方法
    private  List<Tag> tagNames2Tags(String tagNames){
        String[] names=tagNames.split(",");
        List<Tag> tags = new ArrayList<>();
        Map<String,Tag> name2TagMap=tagService.getNameToTagMap();
        for (String s:names){
            Tag t=name2TagMap.get(s);
            tags.add(t);
        }
        return tags;
    }

    @Override
    public Integer countQuestionsByUserId(Integer userId) {
        QueryWrapper<Question> query = new QueryWrapper<>();
        query.eq("user_id",userId);
        query.eq("delete_status",0);
        // 查询问题数
        Integer count = questionMapper.selectCount(query);
        return count;
    }

    @Override
    public PageInfo<Question> getQuestionsByTeacherName(String username,Integer pageNum,Integer pageSize) {
        User user = getUser(username);
        PageHelper.startPage(pageNum,pageSize);
        List<Question> questions = questionMapper.findTeacherQuestions(user.getId());
        log.debug("找到当前用户的问题数:{}",questions.size());
        questions.forEach(q->q.setTags(tagNames2Tags(q.getTagNames()))
        );
        return new PageInfo<>(questions);
    }

    @Override
    public Question getQuestionById(Integer id) {
        Question question = questionMapper.selectById(id);
        // 根据问题的所有标签字符串获得所有标签对象并赋值
        List<Tag> tags = tagNames2Tags(question.getTagNames());
        question.setTags(tags);
        return question;
    }

    private User getUser(String username){
        String url = "http://sys-service/v1/auth/user?username={1}";
        User user = restTemplate.getForObject(url,User.class,username);
        return user;
    }
}
