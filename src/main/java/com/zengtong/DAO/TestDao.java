package com.zengtong.DAO;

import com.zengtong.Service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by znt on 17-7-18.
 */

@Service
public class TestDao {

    @Autowired
    private TestService testService;

    public String say(){
        return "I am TestDao <br>" + testService.say();
    }
}
