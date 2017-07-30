package com.zengtong.Utils;

import com.alibaba.fastjson.JSON;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

@Component
public class JedisAdaptor implements InitializingBean {

    private static final org.slf4j.Logger logger =  LoggerFactory.getLogger(JedisAdaptor.class);

    private Jedis jedis = null;

    private JedisPool pool = null;

    @Override
    public void afterPropertiesSet() throws Exception {

        pool = new JedisPool("localhost",6379);

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
