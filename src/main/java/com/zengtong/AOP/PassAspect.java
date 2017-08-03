package com.zengtong.AOP;

import com.zengtong.Utils.Tool;
import com.zengtong.model.HostHolder;
import com.zengtong.model.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PassAspect {

    @Autowired
    private HostHolder hostHolder;


    @Pointcut("execution(public * com.zengtong.Controller.FollowController.*(..))")

    public void log(){}


    @Around("log()")
    @LoginRequired
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {


        User user = hostHolder.getUser();

        if (user != null){
            Object ret = joinPoint.proceed();
            return ret;
        }


        return Tool.getJSONString(999,"用户未登陆");
    }

}
