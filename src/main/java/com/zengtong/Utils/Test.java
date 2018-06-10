package com.zengtong.Utils;

import redis.clients.jedis.Jedis;

class Test{


    public static void main(String[] args) {

/*
        Set<String> strings = new HashSet<>();

        strings.add("aaa");
        strings.add("bbb");
        strings.add("ccc");
        strings.add("ddd");
        strings.add("eee");

        for (String str : strings){
            System.out.println(str);
        }
*/
        Jedis jedis = new Jedis();

        long a = jedis.zadd("test",1,"val");
        long b = jedis.zadd("test",2,"val");

        System.out.println(b);


    }



}