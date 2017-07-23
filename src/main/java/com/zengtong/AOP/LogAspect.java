package com.zengtong.AOP;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * Created by znt on 17-7-18.
 */

@Aspect
@Component
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("execution(public * com.zengtong.Controller.NewsController.*(..))")
    public void log(){

    }
    @Before("log()")
    public void BeforeMethod(JoinPoint joinPoint){
        StringBuilder sb = new StringBuilder();

        sb.append("\n"+joinPoint.getSignature() + ":");

        for(Object arg : joinPoint.getArgs()){
            sb.append("\nArgs: " + arg.toString());
        }
        logger.info("\nBefore Method : " + sb.toString());
    }

    @After("log()")
    public void AfterMethod(JoinPoint joinPoint){
        logger.info( joinPoint.getSignature() + " execution completed.\n");
    }
}
