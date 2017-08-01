package com.zengtong.Utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;

class Test{

    private static Jedis jedis = new Jedis();

    public static void main(String[] args) {

        Transaction transaction = new Jedis().multi();

        jedis.set("key","Test");
        jedis.set("key","Test2");
        jedis.set("key","Test3");
        String  string = transaction.discard();

        List<Object> lists = transaction.exec();

        for (Object list : lists)
            System.out.println(list);
    }

}