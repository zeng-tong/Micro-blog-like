package com.zengtong.Utils;

import java.util.HashSet;
import java.util.Set;

class Test{


    public static void main(String[] args) {

        Set<String> strings = new HashSet<>();

        strings.add("aaa");
        strings.add("bbb");
        strings.add("ccc");
        strings.add("ddd");
        strings.add("eee");

        for (String str : strings){
            System.out.println(str);
        }

    }



}