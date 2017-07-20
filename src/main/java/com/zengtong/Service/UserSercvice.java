package com.zengtong.Service;

import com.zengtong.DAO.UserDao;
import com.zengtong.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by znt on 17-7-18.
 */
@Service
public class UserSercvice {
    @Autowired
    private UserDao userDao;
    public User update(String Username,Integer age,String Password){
        User user =  new User(Username,age,Password);
        userDao.addUser(user);
        return user;
    }
}
