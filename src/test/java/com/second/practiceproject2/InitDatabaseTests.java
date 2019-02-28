package com.second.practiceproject2;

import com.second.practiceproject2.mapper.QuestionMapper;
import com.second.practiceproject2.mapper.UserMapper;
import com.second.practiceproject2.model.Question;
import com.second.practiceproject2.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql("/init-schema.sql")
public class InitDatabaseTests {


	@Autowired
	//@SuppressWarnings("SpringJavaAutowiringInspection")
	//public InitDatabaseTests(UserDAO userDAO){
		//this.userDAO = userDAO;
	//}
	//@Autowired(required = false)
	//@Resource
	//@Resource(name= "userMapper")
	private UserMapper userMapper;


	@Autowired
	//@SuppressWarnings("SpringJavaAutowiringInspection")
	//public InitDatabaseTests(QuestionDAO questionDAO){
		//this.questionDAO = questionDAO;
	//}
	//@Resource
	//@Resource(name= "questionMapper")
	private QuestionMapper questionMapper;

	@Test
	public void initDatabase() {
	//public void contextLoads() {
		Random random = new Random();

		for(int i = 0; i < 9; ++i) {
			User user = new User();
			//user.setHeadUrl("");
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
			user.setName(String.format("USER%d", i));
			user.setPassword("");
			user.setSalt("");
			//设置
			userMapper.addUser(user);

			user.setPassword("newpassword");
			//更新
			userMapper.updatePassword(user);

			Question question = new Question();
			question.setCommentCount(i);
			Date date = new Date();
			date.setTime(date.getTime() + 1000*3600*i);
			question.setCreatedDate(date);
			question.setUserId(i+1);
			question.setTitle(String.format("TITLE{%d}", i));
			question.setContent(String.format("emmmm Content %d", i));

			questionMapper.addQuestion(question);
		}

		Assert.assertEquals("newpassword", userMapper.selectById(1).getPassword());
		userMapper.deleteById(1);
		Assert.assertNull(userMapper.selectById(1));

		System.out.print(questionMapper.selectLatestQuestions(0,0,10));

	}

}
