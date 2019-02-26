package com.second.practiceproject2.controller;

import com.second.practiceproject2.aspect.LogAspect;
import com.second.practiceproject2.model.User;
import com.second.practiceproject2.service.AnswerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//@Controller
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    AnswerService answerService;

    //初始页面
    @RequestMapping(path = {"/","/index"})
    @ResponseBody
    public String index(){

        logger.info("Visit Home");
        return answerService.getMessage(2)+"first page";
    }
    /*这里注释掉是对应301/302页面跳转
    public String index(HttpSession httpSession)
    {
        return "first page"+ httpSession.setAttribute("msg");
    }*/

    //练习路径
    @RequestMapping(path = {"/profile/{userId}"},method = {RequestMethod.GET })
    @ResponseBody
    public String profile(@PathVariable("userId") int userId,
                          @RequestParam(value = "page",defaultValue = "1",required = true)int page)
    {
        return String.format("Profile Page of %d, p:%d",userId,page);
    }

    //thymeleaf模板
    @RequestMapping(path = {"/thy"},method = {RequestMethod.GET })
    public String template(Model model)
    {
        model.addAttribute("name", "hello world");
        List<String> colors = Arrays.asList(new String[]{"RED","GREEN","BLUE"});
        model.addAttribute("colors",colors);

        Map<String,String> map = new HashMap<>();
        for(int i = 0; i< 4; ++i)
        {
            map.put(String.valueOf(i),String.valueOf(i*i));
        }
        model.addAttribute("map",map);
        model.addAttribute("user",new User("TIAN"));
        return "home";
    }

    //response和request
    @RequestMapping(path = {"/request"},method = {RequestMethod.GET })
    @ResponseBody
    public String request(Model model, HttpServletResponse response,
                           HttpServletRequest request,
                           HttpSession httpSession,
                          @CookieValue("JSESSIONID") String sessionId)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("CookieValue:"+sessionId);

        sb.append(request.getMethod()+"<br>");
        sb.append(request.getQueryString()+"<br>");

        response.addHeader("oneId","learn");
        response.addCookie(new Cookie("username","tld"));
        return sb.toString();
    }

    //301/302页面跳转不知道哪里错了，先注释掉
    /*@RequestMapping(path = {"/redirect/{code}"},method = {RequestMethod.GET })
    public String RedirectView redirect(@PathVariable("code") int code,                          HttpServletRequest request,
                          HttpSession httpSession)
    {
        httpSession.setAttribute("msg","jump from redirect");

        RedirectView red = new RedirectView("/",true);
        if(code == 301) {
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY );
        }
        return red;
    }*/

    //异常捕获，自己定义的异常，这里是admin管理者参数输入错误异常
    @RequestMapping(path = {"/admin"},method = {RequestMethod.GET })
    @ResponseBody
    public String admin(@RequestParam("key") String key){
        if("password".equals(key)){
            return "I'm an admin";
        }
        throw new IllegalArgumentException("参数错啦");
    }
    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e){
        return "error:"+e.getMessage();
    }

}
