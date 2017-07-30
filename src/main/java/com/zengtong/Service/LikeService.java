package com.zengtong.Service;


import com.zengtong.Utils.JedisAdaptor;
import com.zengtong.Utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private JedisAdaptor jedisAdaptor;
    public long like(int entityId,int entityType,int userId){

        String key = RedisKeyUtil.getLikeKey(entityId,entityType);


        String status = jedisAdaptor.get(RedisKeyUtil.checkLike(entityId,entityType,userId));


        //点过赞,status不为空,则取消点赞
        if (status != null){
            jedisAdaptor.del(RedisKeyUtil.checkLike(entityId,entityType,userId));//恢复未点赞状态
            return jedisAdaptor.decr(key);
        }


        String val = jedisAdaptor.get(key);

        if(val == null) {

            jedisAdaptor.set(key,String.valueOf(1));
            jedisAdaptor.set(RedisKeyUtil.checkLike(entityId,entityType,userId),"liked");
            return 1;
        }


        jedisAdaptor.set(RedisKeyUtil.checkLike(entityId,entityType,userId),"liked");

        return jedisAdaptor.incr(key);


    }

}
