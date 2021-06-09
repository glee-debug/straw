package com.xiaoliu.straw.portal.controller;


import com.xiaoliu.straw.portal.model.Answer;
import com.xiaoliu.straw.portal.service.IAnswerService;
import com.xiaoliu.straw.portal.vo.AnswerVo;
import com.xiaoliu.straw.portal.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiaoliu
 * @since 2021-05-20
 */
@RestController
@RequestMapping("/v1/answers")
@Slf4j
public class AnswerController {
    @Autowired
    private IAnswerService answerService;

    // 编写新增讲师回复的控制器方法
    @PostMapping
    //@PreAuthorize("hasRole('ROLE_TEACHER')") //spring-security自动会给hasrole里的string前面加上ROLE_
    @PreAuthorize("hasAuthority('/question/answer')")
    public R<Answer> postAnswer(@Validated AnswerVo answerVo,
                    BindingResult bindingResult,
                    @AuthenticationPrincipal UserDetails userDetails){
        log.debug("接受到的信息:{}",answerVo);
        if (bindingResult.hasErrors()){
            String error = bindingResult.getFieldError().getDefaultMessage();
            return R.unproecsableEntity(error);
        }
        Answer answer = answerService.saveAnswer(answerVo,userDetails.getUsername());
        return R.created(answer);
    }

    @GetMapping("/question/{id}")
    public R<List<Answer>> getQuestionAnswers(@PathVariable Integer id){
        if (id==null){
            return R.invalidRequest("问题ID不能为空");
        }
        List<Answer> answers = answerService.getAnswerByQuestionID(id);
        return R.ok(answers);
    }

    @GetMapping("/{id}/solved")
    public R solved(@PathVariable Integer id){
        boolean accept = answerService.accept(id);
        if (accept){
            return R.ok("采纳答案");
        }else {
            return R.notFound("采纳失败");
        }
    }

}
