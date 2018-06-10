package com.zengtong.Utils;

import com.alibaba.fastjson.JSON;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class JedisAdaptor implements InitializingBean {

    private static final org.slf4j.Logger logger =  LoggerFactory.getLogger(JedisAdaptor.class);

    private Jedis jedis = null;

    private JedisPool pool = null;

    @Override
    public void afterPropertiesSet() throws Exception {

        pool = new JedisPool("localhost",6379);

    }

    public long zcard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zcard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Long zremove(String key,String member){
        Jedis jedis = null;

        try {
            jedis = pool.getResource();

            return jedis.zrem(key,member);

        }catch (Exception e){
            logger.error("发生异常" + e.getMessage());
            return null;
        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
    }

    public Double zscore(String key,String member){
        Jedis jedis = null;

        try {
            jedis = pool.getResource();

            return jedis.zscore(key,member);
        }catch (Exception e){
            logger.error("发生异常" + e.getMessage());
            return null;
        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
    }

    public Set<String> zrevrange(String key, int start, int end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrange(key, start, end);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public String get(String key){

        Jedis jedis = null;
        try {

            jedis = pool.getResource();

            return jedis.get(key);

        }catch (Exception e){
            logger.info("发生异常" + e.getMessage());
            return null;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public long incr(String key){
        Jedis jedis = null;
        try {

            jedis = pool.getResource();

            return jedis.incr(key);

        }catch (Exception e){
            logger.info("发生异常 " + e.getMessage());
            return 0;
        }finally {

            if(jedis != null){
                jedis.close();
            }

        }
    }
    public long decr(String key){
        Jedis jedis = null;
        try {

            jedis = pool.getResource();

            return jedis.decr(key);

        }catch (Exception e){
            logger.info("发生异常 " + e.getMessage());
            return 0;
        }finally {

            if(jedis != null){
                jedis.close();
            }

        }
    }
    public Double cancelLike(String key,String member){
        Jedis jedis = null;
        try {

            jedis = pool.getResource();

            Double likeCount = jedis.zscore(key,member);

            jedis.zadd(key,likeCount-1,member);

            jedis.zrem(key,member);

            return likeCount - 1;
        }catch (Exception e){
            logger.info("发生异常 " + e.getMessage());
            return null;
        }finally {

            if(jedis != null){
                jedis.close();
            }

        }
    }


    public Double Like(String key,String member){
        Jedis jedis = null;
        try {

            jedis = pool.getResource();

            Double likeCounts = jedis.zscore(key,member);

            if (likeCounts == null) jedis.zadd(key,1,member);

            else jedis.zadd(key, likeCounts + 1, member);

            return jedis.zscore(key,member);
        }catch (Exception e){
            logger.info("发生异常 " + e.getMessage());
            return null;
        }finally {

            if(jedis != null){
                jedis.close();
            }

        }
    }

    public long del(String key){
        Jedis jedis = null;
        try {

            jedis = pool.getResource();

            return jedis.del(key);

        }catch (Exception e){
            logger.info("发生异常 " + e.getMessage());
            return 0;
        }finally {

            if(jedis != null){
                jedis.close();
            }

        }
    }

    public String set(String key,String value){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.set(key,value);
        }catch (Exception e){
            logger.info("发生异常" + e.getMessage());
            return null;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public long sadd(String key, String value){
        Jedis jedis = null;
        try {

            jedis = pool.getResource();

            return jedis.sadd(key,value);

        }catch (Exception e){

            logger.info("发生异常" + e.getMessage());
            return 0;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public long srem(String key,String value){
        /*
        *
        * 在key集合(set)中移除指定的元素，如果指定的元素不是key集合中的元素则忽略。
        * */

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key,value);
        }catch (Exception e){
            logger.info("发生异常" + e.getMessage());
            return 0;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public boolean sismember(String key,String value){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key,value);
        }catch (Exception e){
            logger.info("发生异常" + e.getMessage());
            return false;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public long scard(String key){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        }catch (Exception e){
            logger.info("发生异常" + e.getMessage());
            return 0;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    // 验证码, 防机器注册，记录上次注册时间，有效期3天
    public void setex(String key, String value){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.setex(key,30,value);
        }catch (Exception e){
            logger.info("发生异常" + e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public boolean zismember(String key,String value){

        Jedis jedis = null;
        try {
            jedis = pool.getResource();

            Long statu = jedis.zrank(key,value);

            if (statu == null) return false;

            return true;
        }catch (Exception e){
            logger.info("发生异常" + e.getMessage());
            return false;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public long lpush(String key,String  value){

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key,value);
        }catch (Exception e){
            logger.info("发生异常" + e.getMessage());
            return 0;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }



    public long zadd(String key,double score,String value){

        Jedis jedis = null;

        try{
            jedis = pool.getResource();

            return jedis.zadd(key,score,value);
        }
        catch (Exception e){

            e.printStackTrace();

            return 0;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }



    /*
    * RPOP的阻塞版本，这个命令会给在给定的list无法弹出任何元素的时候阻塞连接。
    * */
    public List<String > brpop(int timeout,String key){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout,key);
        }catch (Exception e){
            logger.info("发生异常" + e.getMessage());
            return null;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public void followTransaction(int userID,int entityType,int entityID){

        Jedis jedis = null;

        try {

            jedis = pool.getResource();

            Transaction transaction = new Jedis().multi();

            String key = RedisKeyUtil.getBizFollowlistKey(userID);
            //关注列表里加上user
            jedis.zadd(key,new Date().getTime(),String .valueOf(entityID));

            jedis.zadd(RedisKeyUtil.getBizFanslistKey(entityID),new Date().getTime(),String .valueOf(userID));
            transaction.exec();

        }catch (Exception e){
            logger.info("发生异常: " + e.getMessage());
        }finally {
            if (jedis != null)
                jedis.close();
        }

    }

    public Set<String> zrange(String key, int start, int end){

        Jedis jedis = null;

        try {

            jedis = pool.getResource();
            Set<String> ret = jedis.zrange(key,start,end);

            return ret;
        }catch (Exception e){
            logger.info("发生异常: " + e.getMessage());
            return null;
        }finally {
            if (jedis != null)
                jedis.close();
        }

    }




    public void setObject(String key,Object obj){
        set(key, JSON.toJSONString(obj));
    }

    public <T> T getObject(String key,Class<T> clas){
        String val = get(key);

        if (val != null){
            return JSON.parseObject(val,clas);
        }
        return null;
    }




}
