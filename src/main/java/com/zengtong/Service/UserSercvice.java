package com.zengtong.Service;

import com.zengtong.DAO.UserDao;
import com.zengtong.Utils.Tool;
import com.zengtong.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Created by znt on 17-7-18.
 */
@Service
public class UserSercvice {
    @Autowired
    private UserDao userDao;

    @Autowired
    private TicketService ticketService;



    public boolean register(String Username, String Password,String email,Map<String ,Object> res){

        // to do 判断email是否合法


        User user = userDao.selectByName(Username);

        if(user != null){
            res.put("error","Already exist Username");
            return false;
        }

        String salt = UUID.randomUUID().toString().substring(0,5);

        String head_url = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));

        String pwd = Tool.MD5(Password + salt);

        user =  new User(Username,pwd,salt,head_url,email);


        userDao.addUser(user);

        res.put("ticket",ticketService.addTicket(user.getId()));

        res.put("msg","注册成功");

        return true;
    }

    public boolean login(String email,String password,Map<String ,Object> res){

        if(StringUtils.isBlank(email)){
            res.put("msg","Email is null");
            return false;
        }

        if (StringUtils.isBlank(password)) {
            res.put("msg", "password is null");
            return false;
        }

        User user = userDao.selectByEmail(email);

        String pwd = Tool.MD5(password + user.getSalt() );

        if(user.getPassword().equals(pwd)){

            res.put("ticket",ticketService.addTicket(user.getId()));
            res.put("userid", user.getId());
            res.put("name", user.getName());
            res.put("msg", "登录成功");


            return true;
        }

        res.put("msg","Email or password not match.");

        return false;
    }

}
