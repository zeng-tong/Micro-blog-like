package com.zengtong.Controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zengtong.DAO.UserDao;
import com.zengtong.Service.MessageService;
import com.zengtong.Service.UserSercvice;
import com.zengtong.Utils.Tool;
import com.zengtong.model.HostHolder;
import com.zengtong.model.Message;
import com.zengtong.model.User;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
public class MessageController {

    private static Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "/addMessage",method = RequestMethod.POST)
    @ResponseBody
    public String addMessage(@RequestParam(value = "content") String content,
                             @RequestParam(value = "fromId", defaultValue = "0") int fromId,
                             @RequestParam(value = "toId", defaultValue = "0") int toId,
                             @RequestParam(value = "toName")String toName){

        // 判断用户是否登录
        if(hostHolder.getUser() == null) {
            return Tool.GetJSONString(false, "用户未登录");
        }

        User toUser = userDao.selectByName(toName);

        // 判断名字对应的用户是否存在
        if(toUser == null){
            return Tool.GetJSONString(false,"发送的用户不存在");
        }

        Map map = messageService.addMessage(content,hostHolder.getUser().getId(),toUser.getId());


        if(map == null) return Tool.GetJSONString(false,"发送失败");

//        return Tool.GetJSONString(true,"发送成功");
        return Tool.GetJSONString(true);
    }

    @RequestMapping("/messagebox")
    public String index(Model model, @RequestParam(value = "offset", defaultValue = "0") int offset,
                        @RequestParam(value = "count", defaultValue = "10") int count,
                        HttpServletResponse response) {
        try {
            if (hostHolder.getUser() == null) {
                response.sendRedirect("redirect:/");
                return null;
            } else {
                model.addAttribute("user",hostHolder.getUser());
            }

            List<Message> conversations = messageService.showListMessage(hostHolder.getUser().getId());
            model.addAttribute("messages", buildMessages(conversations, true));

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return "letter";
    }

/*
    @RequestMapping(value = "/showListMessage")
    @ResponseBody
    public String showListMessage(){

        if(hostHolder.getUser() == null){
            return Tool.getJSONString(999,"用户未登录");
        }

        JSONArray jsons = messageService.showListMessage(hostHolder.getUser().getId());

        if(jsons.isEmpty()) return Tool.getJSONString(1,"没有消息");

        return jsons.toJSONString();

    }*/

//    @RequestMapping(value = "/showDetailMessage")
    @RequestMapping(value = "/conversation")
//    @ResponseBody
    public  String showDetailMessage(@RequestParam(value = "id")String conversationId,
                                     HttpServletResponse response,
                                     Model model){

        try {
            if (hostHolder.getUser() == null || StringUtils.isBlank(conversationId) || ArrayUtils.indexOf(conversationId.split("_"),
                    String.valueOf(hostHolder.getUser().getId())) < 0) {
                // 没有权限
                response.sendRedirect("redirect:/");
                return null;
            }

            List<Message> messages = messageService.showDetailMessage(conversationId);

            model.addAttribute("messages", buildMessages(messages, false));
            model.addAttribute("user",hostHolder.getUser());

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return "letterDetail";

   /*     if(hostHolder.getUser() == null){
            return Tool.getJSONString(999,"用户未登录");
        }

        JSONArray jsons = messageService.showDetailMessage(conversationId);

        if(jsons.isEmpty()) return Tool.getJSONString(1,"没有消息");

        return Tool.getJSONString(0,jsons.toJSONString());*/
    }


    @Autowired
    private UserSercvice userSercvice;


    private JSONArray buildMessages(List<Message> messages, boolean isConversation) {
        JSONArray messageArray = new JSONArray();
        for (Message message : messages) {
            User user = null;
            JSONObject obj = new JSONObject();

            if(isConversation) {
                user = userDao.selectById(message.getFromId() + message.getToId() - hostHolder.getUser().getId());
                obj.put("cid", message.getConversationId());
            } else {
                user = userDao.selectById(message.getFromId());
            }

            if (user == null) {
                continue;
            }

            obj.put("mid", message.getId());
            obj.put("content", message.getContent());
            obj.put("createdDate", message.getCreatedDate());
            obj.put("username", user.getName());
            obj.put("userid", user.getId());
            obj.put("userhead", user.getHeadUrl());
            messageArray.add(obj);
        }
        return messageArray;
    }

}
