package com.zengtong.Controller;


import com.zengtong.Utils.JedisAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {

    @Autowired
    private JedisAdaptor jedisAdaptor;


    @RequestMapping(value = "/like")
    @ResponseBody
    public String like(){

        //To do : 点赞功能
        return null;

    }



}
