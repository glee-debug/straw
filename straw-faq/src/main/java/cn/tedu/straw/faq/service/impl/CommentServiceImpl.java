package cn.tedu.straw.faq.service.impl;


import cn.tedu.straw.commons.model.Comment;
import cn.tedu.straw.commons.model.User;
import cn.tedu.straw.commons.service.ServiceException;
import cn.tedu.straw.faq.mapper.CommentMapper;
import cn.tedu.straw.faq.ribbon.RibbonClient;
import cn.tedu.straw.faq.service.ICommentService;
import cn.tedu.straw.faq.vo.CommentVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
    RibbonClient ribbonClient = new RibbonClient();
    @Override
    @Transactional
    public Comment saveComment(CommentVo commentVo, String username) {
        User user = ribbonClient.getUser(username);
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
        User user = ribbonClient.getUser(username);
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
        User user = ribbonClient.getUser(username);
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
