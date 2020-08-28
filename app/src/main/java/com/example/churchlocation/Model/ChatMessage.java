package com.example.churchlocation.Model;

public class ChatMessage {
    private String id, text, fromId, toId;
    private Long time;

    public ChatMessage() {
    }

    public ChatMessage(String id, String text, String fromId, String toId, Long time) {
        this.id = id;
        this.text = text;
        this.fromId = fromId;
        this.toId = toId;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
