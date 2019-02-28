package com.second.practiceproject2.service;

import com.second.practiceproject2.mapper.QuestionMapper;
import com.second.practiceproject2.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    //@Autowired()
    //@Qualifier("questionMapper")
    //@Resource(name="questionDAO")
    private QuestionMapper questionMapper;

    public List<Question> getLatestQuestions(int userId, int offset, int limit){
        return questionMapper.selectLatestQuestions(userId, offset, limit);
    }

}
