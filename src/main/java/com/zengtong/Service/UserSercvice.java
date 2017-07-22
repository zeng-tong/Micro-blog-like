package com.zengtong.Service;

import com.zengtong.DAO.UserDao;
import com.zengtong.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by znt on 17-7-18.
 */
@Service
public class UserSercvice {
    @Autowired
    private UserDao userDao;


    public Map<String,Object> update(String Username, Integer age, String Password){

        Map<String,Object> map = new HashMap<String ,Object>();


        User user = userDao.selectByUsername(Username);


        if(user != null){
            map.put("msgUsn","Already exist Username");
            return map;
        }

        user =  new User(Username,age,Password);
        userDao.addUser(user);


        map.put("success","Register success");

        return map;
    }

    public User getByUsername(String Username){

        if(StringUtils.isBlank(Username))
            return null;

         User user = userDao.selectByUsername(Username);

         return user;
    }
}
