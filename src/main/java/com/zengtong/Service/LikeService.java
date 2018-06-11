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
    public Double like(int entityId,int entityType,int userId){

        String key = RedisKeyUtil.getLikeKey(entityType,entityId);
        //              type ID        userID     likeCount
        // Key  :   LIKE:0:10    value:           score
        //点赞数量
        //如果没有数据记录,则添加一条,初始化喜欢数为１
//            //点过赞则添加一条数据用于检测是否点赞,避免重复点赞
//            jedisAdaptor.zadd(RedisKeyUtil.checkLike(entityType,entityId),new Date().getTime(),String.valueOf(userId));
        switch (entityType){
            case 0 : weiboDao.addLikeCount(entityId);break;
            case 1 : commentDao.addLikeCount(entityId); break;
        }
        return jedisAdaptor.Like(key,String .valueOf(userId));
    }

    public Double dislike(int userId,int entityType,int entityId){

        String key = RedisKeyUtil.getLikeKey(entityType,entityId);

        switch (entityType){
            case 0 : weiboDao.minusLikeCount(entityId);
            case 1 : commentDao.minusLikeCount(entityId);
        }

        return jedisAdaptor.cancelLike(key,String .valueOf(userId));
    }

    public boolean isLiked(int entityType,int entityId,int userId){
        //              type         userID     likeCount
        // Key  :   LIKE:0    value:           score

        return jedisAdaptor.zismember(RedisKeyUtil.getLikeKey(entityType,entityId),String .valueOf(userId));

    }

}
