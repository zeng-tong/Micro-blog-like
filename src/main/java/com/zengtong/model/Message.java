package com.zengtong.model;


import java.util.Date;

public class Message {

    private int id;

    private String content;

    private int fromId;

    private int toId;

    private String conversationId;

    private int hasRead;

    private int fromDelete;

    private int toDelete;

    private Date createdDate;

    public Message() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(int from_id,int to_id) {
        this.conversationId = String.format("%d_%d",Math.min(to_id,from_id),Math.max(to_id,from_id));
    }

    public int getHasRead() {
        return hasRead;
    }

    public void setHasRead(int hasRead) {
        this.hasRead = hasRead;
    }

    public int getFromDelete() {
        return fromDelete;
    }

    public void setFromDelete(int fromDelete) {
        this.fromDelete = fromDelete;
    }

    public int getToDelete() {
        return toDelete;
    }

    public void setToDelete(int toDelete) {
        this.toDelete = toDelete;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }


}
