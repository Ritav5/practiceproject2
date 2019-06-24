package com.second.practiceproject2.service;

import com.second.practiceproject2.mapper.LoginTicketMapper;
import com.second.practiceproject2.mapper.UserMapper;
import com.second.practiceproject2.model.LoginTicket;
import com.second.practiceproject2.model.User;
import com.second.practiceproject2.util.AnswerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.*;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    //@Autowired()
    //@Qualifier("userMapper")
    //@Resource(name="userMapper")
    private UserMapper userMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    public User selectByName(String name) {
        return userMapper.selectByName(name);
    }

    public Map<String, Object> register(String username, String password) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isEmpty(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }

        if (StringUtils.isEmpty(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }

        User user = userMapper.selectByName(username);

        if (user != null) {
            map.put("msg", "用户名已经被注册");
            return map;
        }

        // 密码强度
        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        //https://images.nowcoder.com/head/814m.png@0e_200w_200h_0c_1i_1o_90Q_1x.png
        String head = String.format("http://images.nowcoder.com/head/%dm.png", new Random().nextInt(1000));
        user.setHeadUrl(head);
        user.setPassword(AnswerUtil.MD5(password+user.getSalt()));
        userMapper.addUser(user);

        // 登陆
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    public Map<String, Object> login(String username, String password) {
        Map<String, Object> map = new HashMap<String, Object>();
        //StringUtils判断
        if (StringUtils.isEmpty(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }

        if (StringUtils.isEmpty(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }

        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("msg", "用户名不存在");
            return map;
        }

        if (!AnswerUtil.MD5(password+user.getSalt()).equals(user.getPassword())) {
            map.put("msg", "密码不正确");
            return map;
        }

        String ticket = addLoginTicket(user.getId());//关联ticket
        map.put("ticket", ticket);
        return map;
    }

    private String addLoginTicket(int userId) {
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime() + 1000*3600*24);//有效期
        ticket.setExpired(date);
        ticket.setStatus(0);//状态
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        LoginTicketMapper.addTicket(ticket);
        return ticket.getTicket();
    }


    public User getUser(int id) {
        return userMapper.selectById(id);
}
    public void logout(String ticket) {
        LoginTicketMapper.updateStatus(ticket, 1);
    }

}
