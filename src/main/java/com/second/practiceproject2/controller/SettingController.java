package com.second.practiceproject2.controller;

import com.second.practiceproject2.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller

public class SettingController {
    @Autowired
    AnswerService answerService;
    @RequestMapping(path = {"/setting"},method = {RequestMethod.GET})
    @ResponseBody
    public String index()
    {
        return "Setting Ready" + answerService.getMessage(1);
    }
}
