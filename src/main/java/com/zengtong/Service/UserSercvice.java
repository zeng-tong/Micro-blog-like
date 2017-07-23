package com.zengtong.Service;

import com.zengtong.DAO.UserDao;
import com.zengtong.Utils.Tool;
import com.zengtong.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

    public Map<String,Object> register(String Username, String Password){

        Map<String,Object> map = new HashMap<String ,Object>();


        User user = userDao.selectByName(Username);


        if(user != null){
            map.put("failed","Already exist Username");
            return map;
        }

        String salt = UUID.randomUUID().toString().substring(0,5);

        String head_url = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));

        String pwd = Tool.MD5(Password + salt);

        user =  new User(Username,pwd,salt,head_url);


        userDao.addUser(user);


        map.put("user",user);

        return map;
    }

    public Map<String,String> login(String Username,String password){
        Map<String ,String> map = new HashMap<>();

        if(StringUtils.isBlank(Username)){
            map.put("error","Username is null");
            return map;
        }

        User user = userDao.selectByName(Username);

        String pwd = Tool.MD5(password + user.getSalt() );

        if(user.getPassword().equals(pwd)){

//         Map<String,String> addTicket(int userId) ;

            map = ticketService.addTicket(user.getId());




            return map;
        }

        map.put("error","Username or password not match.");

        return map;
    }

}
