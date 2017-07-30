package com.zengtong.model;

/**
 * Created by znt on 17-7-18.
 */


public class User {
    public User(String name, String password, String salt, String head_url,String email) {
        this.name = name;
        this.password = password;
        this.salt = salt;
        this.head_url = head_url;
        this.email = email;
    }

    public User(){}

    private String email;

    private int id;

    private String name;

    private String password;

    private String salt;

    private String head_url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
