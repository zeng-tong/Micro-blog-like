package com.zengtong.Service;


import com.zengtong.DAO.CommentDao;
import com.zengtong.DAO.WeiboDao;
import com.zengtong.Utils.JedisAdaptor;
import com.zengtong.Utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private WeiboDao weiboDao;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private JedisAdaptor jedisAdaptor;
    public long like(int entityId,int entityType,int userId){

        String key = RedisKeyUtil.getLikeKey(entityId,entityType);


        String status = jedisAdaptor.get(RedisKeyUtil.checkLike(entityId,entityType,userId));


        //点过赞,status不为空,则取消点赞
        if (status != null){
            jedisAdaptor.del(RedisKeyUtil.checkLike(entityId,entityType,userId));//恢复未点赞状态

            switch (entityType){
                case 0 : weiboDao.minusLikeCount(entityId);
                case 1 : commentDao.minusLikeCount(entityId);
            }

            return jedisAdaptor.decr(key);
        }

        //点赞数量
        String val = jedisAdaptor.get(key);

        if(val == null) {

            jedisAdaptor.set(key,String.valueOf(1));
            //点过赞则添加一条数据,避免重复点赞
            jedisAdaptor.set(RedisKeyUtil.checkLike(entityId,entityType,userId),"liked");

            switch (entityType){
                case 0 : weiboDao.addLikeCount(entityId);
                case 1 : commentDao.addLikeCount(entityId);
            }

            return 1;

        }


        jedisAdaptor.set(RedisKeyUtil.checkLike(entityId,entityType,userId),"liked");

        switch (entityType){
            case 0 : weiboDao.addLikeCount(entityId);break;
            case 1 : commentDao.addLikeCount(entityId); break;
        }

        return jedisAdaptor.incr(key);


    }

}
