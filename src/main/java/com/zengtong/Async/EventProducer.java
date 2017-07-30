package com.zengtong.Async;


import com.alibaba.fastjson.JSONObject;
import com.zengtong.Utils.JedisAdaptor;
import com.zengtong.Utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {

    @Autowired
    private JedisAdaptor jedisAdaptor;

    public boolean fireEvent(EventModel model){

        try {

            String json = JSONObject.toJSONString(model);

            String key = RedisKeyUtil.getEventQueueKey();

            jedisAdaptor.lpush(key,json);

            return true;
        }catch (Exception e){
            return false;
        }

    }

}