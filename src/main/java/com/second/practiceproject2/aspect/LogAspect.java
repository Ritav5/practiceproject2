package com.second.practiceproject2.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
//对访问index和setting两个controller的方法做切面截获，
//所有调用index和setting方法之前都调用beforeMethod，之后都调用afterMethod
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);
    @Before("execution(* com.second.practiceproject2.controller.*.*(..))")
    public void beforeMethod(JoinPoint joinPoint){
        StringBuilder sb = new StringBuilder();
        for(Object arg : joinPoint.getArgs()){
            sb.append("arg:" + arg.toString() + "|");
        }
        logger.info("before method:" + new Date() + sb.toString());
    }
    @After("execution(* com.second.practiceproject2.controller.*.*(..))")
    public void afterMethod(){
        logger.info("after method:" + new Date());
    }
}
