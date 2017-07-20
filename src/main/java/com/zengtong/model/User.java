package com.zengtong.model;

import org.springframework.stereotype.Service;

/**
 * Created by znt on 17-7-18.
 */


public class User {
    public User(String username, Integer age, String password) {
        Username = username;
        Age = age;
        Password = password;
    }

    public User() {
    }

    private String Username;

    private Integer Age;

    private String Password;

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public Integer getAge() {
        return Age;
    }

    public void setAge(Integer age) {
        Age = age;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
