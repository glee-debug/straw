package cn.tedu.straw.faq.controller;


import cn.tedu.straw.commons.model.Question;
import cn.tedu.straw.commons.service.ServiceException;
import cn.tedu.straw.commons.vo.R;
import cn.tedu.straw.faq.service.IQuestionService;
import cn.tedu.straw.faq.vo.QuestionVo;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiaoliu
 * @since 2021-05-20
 */
@RestController
@RequestMapping("/v1/questions")
@Slf4j
public class QuestionController {

    @Autowired
    IQuestionService questionService;

    @GetMapping("/my")
    public R<PageInfo<Question>> my(@AuthenticationPrincipal UserDetails userDetails, Integer pageNum){
        log.debug("开始查询当前登录用户问题列表,{}",userDetails.getUsername());
        if (pageNum==null){
            pageNum=1;
        }
        Integer pageSize = 8;
        PageInfo<Question> pageInfo=questionService.getMyQuestions(userDetails.getUsername(),pageNum,pageSize);
        return R.ok(pageInfo);
    }

    @PostMapping("")
    public R createQuestion(
            @Validated QuestionVo questionVo, BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails
            ){
        log.debug("获得的问题信息:{}",questionVo);
        if (bindingResult.hasErrors()){
            String error = bindingResult.getFieldError().getDefaultMessage();
            return R.unproecsableEntity(error);
        }
        try {
            questionService.saveQuestion(questionVo,userDetails.getUsername());
            return R.ok("新增问题完成");
        }catch (ServiceException e){
            log.error("保存失败",e);
            return R.failed(e);
        }
    }

    @GetMapping("/teacherQuestions")
    // 运行方法必须有role_teacher角色
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public R<PageInfo<Question>> teacherQuestions(
            @AuthenticationPrincipal UserDetails userDetails, Integer pageNum){
        log.debug("开始查询当前登录用户问题列表,{}",userDetails.getUsername());
        if (pageNum==null){
            pageNum=1;
        }
        Integer pageSize = 8;
        PageInfo<Question> pageInfo=questionService.getQuestionsByTeacherName(
                userDetails.getUsername(),pageNum,pageSize);
        return R.ok(pageInfo);
    }

    // springmvc允许控制器使用{id}作为路径占位符
    @GetMapping("/{id}")
    public R<Question> question(@PathVariable Integer id){
        if (id==null){
            return R.invalidRequest("ID不能为空");
        }
        Question question=questionService.getQuestionById(id);
        return R.ok(question);
    }

    // 根据用户id查询问题数
    @GetMapping("/count")
    public Integer count(Integer userId){
        return questionService.countQuestionsByUserId(userId);
    }
}
