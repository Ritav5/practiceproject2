package com.second.practiceproject2.controller;


import com.second.practiceproject2.model.Question;
import com.second.practiceproject2.service.QuestionService;
import com.second.practiceproject2.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    //初始页面
    @RequestMapping(path = {"/","/index"})
    public String index(Model model){
        List<Question> questionList = questionService.getLatestQuestions(0,0,10);
        model.addAttribute("question", questionList);
        return "index";
    }
}
