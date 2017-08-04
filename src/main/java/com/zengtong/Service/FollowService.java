package com.zengtong.Service;


import com.zengtong.Async.EventProducer;
import com.zengtong.Utils.JedisAdaptor;
import com.zengtong.Utils.RedisKeyUtil;
import com.zengtong.Utils.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    public long getFolloweeCount(int userId, int entityType) {
        String followeeKey = RedisKeyUtil.getBizFollowlistKey(userId);
        return jedisAdaptor.zcard(followeeKey);
    }


    public List<Integer> getFollowees(int userId, int offset, int count) {
        String followeeKey = RedisKeyUtil.getBizFollowlistKey(userId);

        return getIntegerIds(jedisAdaptor.zrevrange(followeeKey, offset, offset+count));
    }

    public boolean isFollower(int myID,int userID){

        String key = RedisKeyUtil.getBizFollowlistKey(myID);

        if (jedisAdaptor.zismember(key,String.valueOf(userID))) return true;

        return false;
    }

    private List<Integer> getIntegerIds(Set<String> ss) {
        List<Integer> listI = new ArrayList<>();
        for (String s : ss) {
            listI.add(Integer.parseInt(s));
        }
        return listI;
    }
}
