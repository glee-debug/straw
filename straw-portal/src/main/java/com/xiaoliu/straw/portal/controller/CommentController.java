package com.xiaoliu.straw.portal.controller;


import com.xiaoliu.straw.portal.model.Comment;
import com.xiaoliu.straw.portal.service.ICommentService;
import com.xiaoliu.straw.portal.vo.CommentVo;
import com.xiaoliu.straw.portal.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiaoliu
 * @since 2021-05-20
 */
@RestController
@RequestMapping("/v1/comments")
@Slf4j
public class CommentController {
    @Resource
    private ICommentService commentService;

    @PostMapping
    public R<Comment> postComment(
            @Validated CommentVo commentVo,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails
            ){
        log.debug("收到的评论信息:{}",commentVo);
        if (bindingResult.hasErrors()){
            String error = bindingResult.getFieldError().getDefaultMessage();
            return R.invalidRequest(error);
        }
        Comment comment = commentService.saveComment(commentVo,userDetails.getUsername());
        return R.created(comment);
    }

    @GetMapping("/{commentId}/delete")
    public R deleteComment(@PathVariable Integer commentId,
                           @AuthenticationPrincipal UserDetails userDetails){
        if (commentId==null){
            return R.notFound("commentId 不存在");
        }
        Boolean isDelete = commentService.removeComment(commentId,userDetails.getUsername());
        if (isDelete){
            return R.gone("删除成功");
        }else{
            return R.notFound("删除失败");
        }
    }

    @PostMapping("/{id}/update")
    public R<Comment> updateComment(@PathVariable Integer id,String content,
                           @AuthenticationPrincipal UserDetails userDetails){
        Comment comment = commentService.updateComment(id,content,userDetails.getUsername());
        return R.ok(comment);
    }

}

