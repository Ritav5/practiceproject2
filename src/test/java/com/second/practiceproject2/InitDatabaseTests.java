package com.second.practiceproject2;

import com.second.practiceproject2.dao.UserDAO;
import com.second.practiceproject2.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql("/init-schma.sql")
public class InitDatabaseTests {
	@Autowired
	UserDAO userDAO;

	@Test
	public void initDatabase() {
		Random random = new Random();

		for(int i = 0; i < 11; ++i){
			User user = new User();
			user.setHeadUrl(String.format("http://"));
		}

	}

}
