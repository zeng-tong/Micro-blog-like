package com.zengtong.Async.handler;

import com.zengtong.Async.EventHandler;
import com.zengtong.Async.EventModel;
import com.zengtong.Async.EventType;
import com.zengtong.Utils.JedisAdaptor;
import com.zengtong.Utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Component
public class FeedCenterHandler implements EventHandler {

    @Autowired
    private JedisAdaptor jedisAdaptor;

    @Override
    public void doHandler(EventModel model) {

        String key = RedisKeyUtil.getFeedKey(model.getUser_id(),model.getEntity_type());

        String value = RedisKeyUtil.getFeedValue(model.getEntity_type(),model.getEntity_id()); // key = "type:id"

        jedisAdaptor.zadd(key,new Date().getTime(),value);

    }

    @Override
    public List<EventType> getSupportType() {
        return Arrays.asList(EventType.FEEDCENTER);
    }
}
