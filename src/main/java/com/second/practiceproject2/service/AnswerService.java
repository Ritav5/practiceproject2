package com.second.practiceproject2.service;

import org.springframework.stereotype.Service;

@Service
public class AnswerService {
    public String getMessage(int userId){
        return "First Message:"+ String.valueOf(userId);
    }
}
