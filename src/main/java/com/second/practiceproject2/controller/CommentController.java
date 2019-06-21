package com.second.practiceproject2.controller;

import com.second.practiceproject2.model.Comment;
import com.second.practiceproject2.model.EntityType;
import com.second.practiceproject2.model.HostHolder;
import com.second.practiceproject2.service.CommentService;
import com.second.practiceproject2.service.QuestionService;
import com.second.practiceproject2.service.SensitiveService;
import com.second.practiceproject2.service.UserService;
import com.second.practiceproject2.util.AnswerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;


@Controller
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    QuestionService questionService;

    @Autowired
    SensitiveService sensitiveService;

    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content) {
        try {
            content = HtmlUtils.htmlEscape(content);//去掉html标签防止未知跳转
            content = sensitiveService.filter(content);//过滤敏感词
            // 过滤content
            Comment comment = new Comment();
            if (hostHolder.getUser() != null) {
                comment.setUserId(hostHolder.getUser().getId());//用户已登录
            } else {
                comment.setUserId(AnswerUtil.ANONYMOUS_USERID);//用户未登录，匿名用户
                //return "redirect:/reglogin";//跳转登录
            }
            comment.setContent(content);
            comment.setEntityId(questionId);
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);

            commentService.addComment(comment);

            // 更新题目里的评论数量，后期变为异步的
            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            questionService.updateCommentCount(comment.getEntityId(), count);
            // 怎么异步化
        } catch (Exception e) {
            logger.error("增加评论失败" + e.getMessage());
        }
        return "redirect:/question/" + String.valueOf(questionId);
    }
}
