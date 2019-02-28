package com.second.practiceproject2;

import org.mybatis.spring.annotation.MapperScan;
//import tk.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@MapperScan("com.second.practiceproject2.mapper")
@ComponentScan("com.second.practiceproject2.mapper")
public class Practiceproject2Application {

	public static void main(String[] args) {
		SpringApplication.run(Practiceproject2Application.class, args);

	}

}
