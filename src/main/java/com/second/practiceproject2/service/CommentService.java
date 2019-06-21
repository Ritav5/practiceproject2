package com.second.practiceproject2.service;

import com.second.practiceproject2.mapper.CommentMapper;
import com.second.practiceproject2.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;

    public List<Comment> getCommentsByEntity(int entityId, int entityType) {
        return commentMapper.selectByEntity(entityId, entityType);
    }

    public int addComment(Comment comment) {
        return commentMapper.addComment(comment);
    }

    public int getCommentCount(int entityId, int entityType) {
        return commentMapper.getCommentCount(entityId, entityType);
    }

    //controller层的删除命令对应数据库更新状态不删除，所以数据库里是update
    public void deleteComment(int entityId, int entityType) {
        commentMapper.updateStatus(entityId, entityType, 1);
    }
}
