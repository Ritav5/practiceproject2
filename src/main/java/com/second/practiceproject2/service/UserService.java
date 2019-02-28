package com.second.practiceproject2.service;

import com.second.practiceproject2.mapper.UserMapper;
import com.second.practiceproject2.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    //@Autowired()
    //@Qualifier("userMapper")
    //@Resource(name="userMapper")
    private UserMapper userMapper;

    public User getUser(int id) {
        return userMapper.selectById(id);
}

}
