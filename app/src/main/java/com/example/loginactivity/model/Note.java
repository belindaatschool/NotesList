package com.example.loginactivity.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Note {
    String title;
    String content;
    String timestamp;
    String image;

    public Note(String title, String content,String image) {
        this.title = title;
        this.content = content;
        this.timestamp = new SimpleDateFormat("dd/MM/yy  HH:mm").format(new Date());
        this.image = image;
    }
    public Note(){
        this.title = "";
        this.content = "";
        this.image = "";
        this.timestamp = new SimpleDateFormat("dd/MM/yy  HH:mm").format(new Date());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
