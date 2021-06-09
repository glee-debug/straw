package com.xiaoliu.straw.portal.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoliu.straw.portal.mapper.CommentMapper;
import com.xiaoliu.straw.portal.mapper.UserMapper;
import com.xiaoliu.straw.portal.model.Comment;
import com.xiaoliu.straw.portal.model.User;
import com.xiaoliu.straw.portal.service.ICommentService;
import com.xiaoliu.straw.portal.service.ServiceException;
import com.xiaoliu.straw.portal.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiaoliu
 * @since 2021-05-20
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public Comment saveComment(CommentVo commentVo, String username) {
        User user = userMapper.findUserByUsername(username);
        Comment comment = new Comment()
                .setAnswerId(commentVo.getAnswerId())
                .setContent(commentVo.getContent())
                .setUserId(user.getId())
                .setUserNickName(user.getNickname())
                .setCreatetime(LocalDateTime.now());
        int num = commentMapper.insert(comment);
        if (num!=1){
            throw ServiceException.busy();
        }
        return comment;
    }

    @Override
    public boolean removeComment(Integer commentId, String username) {
        User user = userMapper.findUserByUsername(username);
        Comment comment = commentMapper.selectById(commentId);
        if((user.getType()!=null && user.getType()==1) || user.getId().equals(comment.getUserId())){
            // 是讲师 或评论发布者
            int num = commentMapper.deleteById(commentId);
            return num==1;
        }
        throw ServiceException.invalidRequest("删除评论权限不足");
    }

    @Override
    public Comment updateComment(Integer commentId, String content, String username) {
        User user = userMapper.findUserByUsername(username);
        Comment comment = commentMapper.selectById(commentId);
        if ((user.getId()!=null && user.getId()==1) || user.getId().equals(comment.getUserId())){
            comment.setContent(content);
            int num = commentMapper.updateById(comment);
            if (num!=1){
                throw ServiceException.busy();
            }
            return comment;
        }
        throw ServiceException.invalidRequest("修改评论权限不足!");
    }
}
