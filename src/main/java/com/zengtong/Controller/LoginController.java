package com.zengtong.Controller;


import com.alibaba.fastjson.JSONObject;
import com.zengtong.Service.UserSercvice;
import com.zengtong.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @Autowired
    private UserSercvice userSercvice;

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    @ResponseBody
    public String register(@RequestParam("Username")String username,
                           @RequestParam("Age")Integer age,
                           @RequestParam("Password")String password){

        JSONObject json = new JSONObject();

        json.putAll(userSercvice.update(username,age,password));

        return  json.toJSONString();
    }


    @RequestMapping(value = "/get")
    @ResponseBody
    public String  getByUsername(@RequestParam("Username")String Username){

        JSONObject json = new JSONObject();

        if(StringUtils.isBlank(Username)){
            json.put("msg","Username is null.");
            return json.toJSONString();
        }

        User user = userSercvice.getByUsername(Username);

        if(user == null){
            json.put("msg","User not exist.");
        }

       json.put("msg",new JSONObject().fluentPut("Username",user.getUsername()).fluentPut("Age",user.getAge()));

        return json.toJSONString();
    }
}
