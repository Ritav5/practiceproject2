package com.second.practiceproject2.service;

import com.second.practiceproject2.mapper.QuestionMapper;
import com.second.practiceproject2.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    //@Autowired()
    //@Qualifier("questionMapper")
    //@Resource(name="questionDAO")
    private QuestionMapper questionMapper;

    @Autowired
    private SensitiveService sensitiveService;

    public Question getById(int id) {
        return questionMapper.getById(id);
    }

    public int addQuestion(Question question) {
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));//过滤HTML标签，字符转义
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        // 敏感词过滤
        question.setTitle(sensitiveService.filter(question.getTitle()));
        question.setContent(sensitiveService.filter(question.getContent()));
        return questionMapper.addQuestion(question) > 0 ? question.getId() : 0;
    }

    public List<Question> getLatestQuestions(int userId, int offset, int limit){
        return questionMapper.selectLatestQuestions(userId, offset, limit);
    }

    public int updateCommentCount(int id, int count) {
        return questionMapper.updateCommentCount(id, count);
    }
}
