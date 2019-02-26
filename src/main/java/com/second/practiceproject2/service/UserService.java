package com.second.practiceproject2.service;

import com.second.practiceproject2.dao.UserDAO;
import com.second.practiceproject2.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;

    public User getUser(int id) {
        return userDAO.selectById(id);
    }

}
