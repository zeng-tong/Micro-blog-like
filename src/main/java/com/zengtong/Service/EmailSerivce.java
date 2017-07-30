package com.zengtong.Service;

import com.zengtong.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class EmailSerivce {


    @Autowired
    private JavaMailSenderImpl javaMailSender;

    public void registerSuc(String toMail,User user) {


            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom("unixsudo@163.com");

            message.setTo(toMail);

            message.setSubject("SummerCamp微博");

            message.setText("恭喜你 "+ user.getName() + ",微博注册成功.");

            message.setSentDate(new Date());

            message.setReplyTo("register");

            javaMailSender.send(message);

    }

}
