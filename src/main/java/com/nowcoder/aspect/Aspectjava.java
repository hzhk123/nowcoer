package com.nowcoder.aspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class Aspectjava {
    private  static final Logger logger = LoggerFactory.getLogger(Aspectjava.class) ;


    @Before("execution(* com.study.project1.controller.*Controller.*(..))")
    public void before(JoinPoint joinPoint)
    {
        StringBuilder sb = new StringBuilder() ;
        for(Object arg:joinPoint.getArgs())
        {
            sb.append("arg:"+arg) ;
        }
        logger.info("before method:"+sb.toString());
    }

    @After("execution(* com.nowcoder.controller.*Controller.*(..))")
    public void after(JoinPoint joinpoint)
    {
        logger.info("after method:");
    }
}
