package com.second.practiceproject2.interceptor;

import com.second.practiceproject2.mapper.LoginTicketMapper;
import com.second.practiceproject2.mapper.UserMapper;
import com.second.practiceproject2.model.HostHolder;
import com.second.practiceproject2.model.LoginTicket;
import com.second.practiceproject2.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


@Component
public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HostHolder hostHolder;//用户放在hostHolder里面

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ticket = null;
        if (httpServletRequest.getCookies() != null) {
            for (Cookie cookie : httpServletRequest.getCookies()) {//找到ticket
                if (cookie.getName().equals("ticket")) {
                    ticket = cookie.getValue();
                    break;
                }
            }
        }

        if (ticket != null) {//找到后判断是否有效
            LoginTicket loginTicket = loginTicketMapper.selectByTicket(ticket);
            if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0) {
                return true;//不可返回false，否则结束请求
            }

            //提取用户信息，连接上下文，保证其他都可以访问（写component控制反转即可用注解引用）
            User user = userMapper.selectById(loginTicket.getUserId());
            hostHolder.setUser(user);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && hostHolder.getUser() != null) {
            //在渲染之前加此拦截器，可以使所有的controller在渲染之前得到user
            // modelAndView对应controller层的model和templates的view模板，可以在模板里直接访问变量user
            modelAndView.addObject("user", hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //拦截结束的清理
        hostHolder.clear();
    }
}
