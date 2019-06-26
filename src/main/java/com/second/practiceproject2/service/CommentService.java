package com.second.practiceproject2.service;

import com.second.practiceproject2.mapper.CommentMapper;
import com.second.practiceproject2.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;


@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    SensitiveService sensitiveService;

    public List<Comment> getCommentsByEntity(int entityId, int entityType) {
        return commentMapper.selectCommentByEntity(entityId, entityType);
    }

    public int addComment(Comment comment) {
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveService.filter(comment.getContent()));
        return commentMapper.addComment(comment) > 0 ? comment.getId() : 0;
    }

    public int getCommentCount(int entityId, int entityType) {
        return commentMapper.getCommentCount(entityId, entityType);
    }

    //controller层的删除命令对应数据库更新状态不删除，所以数据库里是update
    //public void deleteComment(int entityId, int entityType) {
        //commentMapper.updateStatus(entityId, entityType, 1);
    //}
    public boolean deleteComment(int commentId) {
        return commentMapper.updateStatus(commentId, 1) > 0;
    }

    public Comment getCommentById(int id) {
        return commentMapper.getCommentById(id);
    }
}
