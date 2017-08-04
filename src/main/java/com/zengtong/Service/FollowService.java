package com.zengtong.Service;


import com.zengtong.Async.EventProducer;
import com.zengtong.Utils.JedisAdaptor;
import com.zengtong.Utils.RedisKeyUtil;
import com.zengtong.Utils.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowService {


    @Autowired
    private JedisAdaptor jedisAdaptor;

    @Autowired
    private EventProducer eventProducer;

    public String follow(int myId,int userId){


        if ( isFollower(myId,userId)) return null;

        jedisAdaptor.followTransaction(myId,userId);

        return Tool.getJSONString(0,"关注成功.");

    }

    public boolean isFollower(int myID,int userID){

        String key = RedisKeyUtil.getBizFollowlistKey(myID);

        if (jedisAdaptor.zismember(key,String.valueOf(userID))) return true;

        return false;
    }
}
