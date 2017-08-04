package com.zengtong.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class Weibo {

    private int id;

    private int userId;

    private int commentCount;

    private int status;

    private String picUrl;

    private Date createDate;

    private int likeCount;

    private String content;

    public Weibo(int userId, int commentCount, int status, String picUrl, Date createDate, int likeCount, String content) {
        this.userId = userId;
        this.commentCount = commentCount;
        this.status = status;
        this.picUrl = picUrl;
        this.createDate = createDate;
        this.likeCount = likeCount;
        this.content = content;
    }

    public Weibo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getImageList() {
        if (StringUtils.isBlank(picUrl)) {
            return new String [0];
        }
        return picUrl.split("\\|");
    }
}
