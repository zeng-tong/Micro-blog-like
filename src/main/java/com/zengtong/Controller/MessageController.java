package com.zengtong.Controller;


import com.alibaba.fastjson.JSONArray;
import com.zengtong.DAO.UserDao;
import com.zengtong.Service.MessageService;
import com.zengtong.Utils.Tool;
import com.zengtong.model.HostHolder;
import com.zengtong.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "/addMessage",method = RequestMethod.POST)
    @ResponseBody
    public String addMessage(@RequestParam(value = "content") String content,
                             @RequestParam(value = "toName")String toName){

        // 判断用户是否登录
        if(hostHolder.getUser() == null) {
            return Tool.getJSONString(999, "用户未登录");
        }

        User toUser = userDao.selectByName(toName);

        // 判断名字对应的用户是否存在
        if(toUser == null){
            return Tool.getJSONString(1,"发送的用户不存在");
        }

        Map map = messageService.addMessage(content,hostHolder.getUser().getId(),toUser.getId());


        if(map == null) return Tool.getJSONString(1,"发送失败");

        return Tool.getJSONString(0,"发送成功");
    }


    @RequestMapping(value = "/showDetailMessage")
    @ResponseBody
    public  String showDetailMessage(@RequestParam(value = "conversationId")String conversationId){

        if(hostHolder.getUser() == null){
            return Tool.getJSONString(999,"用户未登录");
        }

        JSONArray jsons = messageService.showDetailMessage(conversationId);

        if(jsons.isEmpty()) return Tool.getJSONString(1,"没有消息");

        return Tool.getJSONString(0,jsons.toJSONString());
    }

    @RequestMapping(value = "/showListMessage")
    @ResponseBody
    public String showListMessage(){

        if(hostHolder.getUser() == null){
            return Tool.getJSONString(999,"用户未登录");
        }

        JSONArray jsons = messageService.showListMessage(hostHolder.getUser().getId());

        if(jsons.isEmpty()) return Tool.getJSONString(1,"没有消息");

        return jsons.toJSONString();

    }

}
