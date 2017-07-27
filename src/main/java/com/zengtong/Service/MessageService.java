package com.zengtong.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zengtong.DAO.MessageDao;
import com.zengtong.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageService {

    @Autowired
    private MessageDao messageDao;



    public Map<String,Object> addMessage(String content,int fromId,int toId){

        Map<String,Object> map = new HashMap<>();

        Message message = new Message();

        message.setContent(content);
        message.setConversationId(fromId,toId); // 安全起见,不掉用message本身的from_id to_id 生成
        message.setFromId(fromId);
        message.setToId(toId);
        message.setHasRead(0); // 0表示未阅读
        message.setFromDelete(0); // 0未删除
        message.setToDelete(0);
        message.setCreateDate(new Date());
        messageDao.addMessage(message);

        map.put("success","发送成功");

        return map;
    }


    public JSONArray showDetailMessage(String conversationID){


        List<Message> messages = messageDao.showDetailMessage(conversationID);

        JSONArray jsons = new JSONArray();

        for (Message message : messages){
            JSONObject json = new JSONObject();
            json.put("content",message.getContent());
            json.put("CreateDate",message.getCreateDate());
            json.put("From : ",message.getFromId());
            json.put("To : ",message.getToId());
            jsons.add(json);
        }

        return jsons;

    }

    public JSONArray showListMessage(int myId){

        List<Message> messages = messageDao.showListMessage(myId);

        JSONArray jsons = new JSONArray();

        if(messages.isEmpty()) return jsons;

        for (Message message : messages){

            JSONObject json = new JSONObject();

            json.put("nums",message.getId());
            json.put("createDate",message.getCreateDate());
            json.put("content",message.getContent());
            json.put("To",message.getToId());
            json.put("From",message.getFromId());

            jsons.add(json);
        }

        return jsons;
    }

}
