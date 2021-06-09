package com.xiaoliu.straw.portal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoliu.straw.portal.model.Comment;
import com.xiaoliu.straw.portal.vo.CommentVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaoliu
 * @since 2021-05-20
 */
public interface ICommentService extends IService<Comment> {
    //新增评论
    Comment saveComment(CommentVo commentVo,String username);

    boolean removeComment(Integer commentId,String username);

    Comment updateComment(Integer commentId,String content,String username);

}
